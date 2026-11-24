package de.dhbw.campusmarket.ui.adlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import de.dhbw.campusmarket.CampusMarketApplication
import de.dhbw.campusmarket.data.Ad
import de.dhbw.campusmarket.data.AdRepository
import de.dhbw.campusmarket.data.Category
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Besitzer des Listen-States. Nur diese Klasse ändert _uiState.
class AdListViewModel(private val repository: AdRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AdListUiState())
    val uiState: StateFlow<AdListUiState> = _uiState.asStateFlow()

    // Merkt sich das laufende Sammeln, damit "Erneut versuchen" es nicht doppelt startet.
    private var loadJob: Job? = null

    init {
        loadAds()
    }

    // Sammelt die Anzeigen aus Room. Weil das DAO einen Flow liefert, kommt nach jeder
    // Änderung der Tabelle (z. B. Löschen) automatisch die neue Liste an.
    fun loadAds() {
        loadJob?.cancel()
        _uiState.update { it.copy(isLoading = true, hasError = false) }
        loadJob = viewModelScope.launch {
            repository.getAllAds()
                .catch { _uiState.update { it.copy(isLoading = false, hasError = true) } }
                .collect { ads ->
                    _uiState.update { it.copy(isLoading = false, hasError = false, ads = ads) }
                }
        }
    }

    // Wird bei jeder Eingabe im Suchfeld aufgerufen. Weil der Suchtext im UiState liegt,
    // bleibt er bei einer Drehung erhalten.
    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }
    }

    // Tipp auf eine Kategorie: wählt sie aus. Ein zweiter Tipp auf dieselbe hebt den Filter auf.
    fun onCategoryFilterClick(category: Category) {
        _uiState.update {
            val newCategory = if (it.categoryFilter == category) null else category
            it.copy(categoryFilter = newCategory)
        }
    }

    fun onOnlyFavoritesClick() {
        _uiState.update { it.copy(onlyFavorites = !it.onlyFavorites) }
    }

    fun onOnlyAvailableClick() {
        _uiState.update { it.copy(onlyAvailable = !it.onlyAvailable) }
    }

    // Herz in der Liste: schaltet den Favoriten um. Die neue Liste kommt danach über den Flow.
    fun onFavoriteClick(ad: Ad) {
        viewModelScope.launch {
            try {
                repository.setFavorite(ad.id, !ad.isFavorite)
            } catch (e: Exception) {
                // Speichern hat nicht geklappt: Das Herz bleibt wie vorher, die App stürzt nicht ab.
            }
        }
    }

    companion object {
        // Baut den ViewModel mit dem Repository aus der Application-Klasse.
        val Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as CampusMarketApplication
                AdListViewModel(application.repository)
            }
        }
    }
}
