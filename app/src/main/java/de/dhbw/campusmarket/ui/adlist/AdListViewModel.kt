package de.dhbw.campusmarket.ui.adlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import de.dhbw.campusmarket.CampusMarketApplication
import de.dhbw.campusmarket.data.Ad
import de.dhbw.campusmarket.data.AdRepository
import de.dhbw.campusmarket.data.Category
import de.dhbw.campusmarket.logic.SortOrder
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "AdListViewModel"

// Owner of the list state. Only this class changes _uiState.
class AdListViewModel(private val repository: AdRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AdListUiState())
    val uiState: StateFlow<AdListUiState> = _uiState.asStateFlow()

    // Remembers the running collection so that "try again" does not start it twice.
    private var loadJob: Job? = null

    init {
        loadAds()
    }

    // Collects the ads from Room. Because the DAO returns a Flow, the new list arrives
    // automatically after every change of the table (e.g. deleting).
    fun loadAds() {
        loadJob?.cancel()
        _uiState.update { it.copy(isLoading = true, hasError = false) }
        loadJob = viewModelScope.launch {
            repository.getAllAds()
                .catch { e ->
                    Log.e(TAG, "Loading the ads failed", e)
                    _uiState.update { it.copy(isLoading = false, hasError = true) }
                }
                .collect { ads ->
                    _uiState.update { it.copy(isLoading = false, hasError = false, ads = ads) }
                }
        }
    }

    // Called on every input in the search field. Because the query lives in the UiState,
    // it survives a rotation.
    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }
    }

    // Tap on a category: selects it. A second tap on the same one removes the filter.
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

    // Entry in the sort menu. The list is sorted again in visibleAds.
    fun onSortOrderChange(sortOrder: SortOrder) {
        Log.d(TAG, "Sort order changed to $sortOrder")
        _uiState.update { it.copy(sortOrder = sortOrder) }
    }

    // Heart in the list: toggles the favorite. The new list then arrives through the Flow.
    fun onFavoriteClick(ad: Ad) {
        viewModelScope.launch {
            try {
                repository.setFavorite(ad.id, !ad.isFavorite)
                Log.d(TAG, "Ad ${ad.id}: favorite set to ${!ad.isFavorite}")
            } catch (e: Exception) {
                // Saving failed: the heart stays as before and the app does not crash.
                Log.e(TAG, "Changing the favorite of ad ${ad.id} failed", e)
            }
        }
    }

    companion object {
        // Builds the ViewModel with the repository from the Application class.
        val Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as CampusMarketApplication
                AdListViewModel(application.repository)
            }
        }
    }
}
