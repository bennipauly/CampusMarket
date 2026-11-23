package de.dhbw.campusmarket.ui.addetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import de.dhbw.campusmarket.CampusMarketApplication
import de.dhbw.campusmarket.data.AdRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Besitzer des Detail-States. Bekommt die id der Anzeige von der Route.
class AdDetailViewModel(
    private val repository: AdRepository,
    private val adId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdDetailUiState())
    val uiState: StateFlow<AdDetailUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadAd()
    }

    // Beobachtet die eine Anzeige in Room. Ändert sie sich, kommt der neue Stand automatisch.
    fun loadAd() {
        loadJob?.cancel()
        _uiState.update { it.copy(isLoading = true, hasError = false) }
        loadJob = viewModelScope.launch {
            repository.getAd(adId)
                .catch { _uiState.update { it.copy(isLoading = false, hasError = true) } }
                .collect { ad ->
                    _uiState.update { it.copy(isLoading = false, hasError = false, ad = ad) }
                }
        }
    }

    // Herz oben rechts: schaltet den Favoriten um. Der neue Stand kommt über den Flow aus Room.
    fun onFavoriteClick() {
        val ad = _uiState.value.ad ?: return
        viewModelScope.launch {
            try {
                repository.setFavorite(ad.id, !ad.isFavorite)
            } catch (e: Exception) {
                // Speichern hat nicht geklappt: Das Herz bleibt wie vorher, die App stürzt nicht ab.
            }
        }
    }

    // Knopf unter den Details: wechselt zwischen "verfügbar" und "verkauft".
    fun onSoldClick() {
        val ad = _uiState.value.ad ?: return
        viewModelScope.launch {
            try {
                repository.setSold(ad.id, !ad.isSold)
            } catch (e: Exception) {
                // Speichern hat nicht geklappt: Der Status bleibt wie vorher, die App stürzt nicht ab.
            }
        }
    }

    fun onDeleteClick() {
        _uiState.update { it.copy(showDeleteDialog = true) }
    }

    fun onDeleteDismiss() {
        _uiState.update { it.copy(showDeleteDialog = false) }
    }

    fun onDeleteConfirm() {
        val ad = _uiState.value.ad ?: return
        _uiState.update { it.copy(showDeleteDialog = false) }
        // Erst das Beobachten beenden. Sonst würde Room nach dem Löschen "null" schicken
        // und der Screen kurz "Anzeige nicht gefunden" zeigen, bevor er zurücknavigiert.
        loadJob?.cancel()
        viewModelScope.launch {
            try {
                repository.deleteAd(ad)
                _uiState.update { it.copy(isDeleted = true) }
            } catch (e: Exception) {
                // Löschen hat nicht geklappt: Fehler zeigen statt abstürzen.
                _uiState.update { it.copy(hasError = true) }
            }
        }
    }

    companion object {
        // Die id kommt als Parameter, darum ist das hier eine Funktion und kein festes Objekt.
        fun factory(adId: Long): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as CampusMarketApplication
                AdDetailViewModel(application.repository, adId)
            }
        }
    }
}
