package de.dhbw.campusmarket.ui.adlist

import android.database.sqlite.SQLiteException
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
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

private const val TAG = "AdListViewModel"

// Owner of the list state. Only this class changes _uiState.
class AdListViewModel(private val repository: AdRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AdListUiState())
    val uiState: StateFlow<AdListUiState> = _uiState.asStateFlow()

    // Remembers the running jobs so that "try again" and the refresh button never start them twice.
    private var refreshJob: Job? = null
    private var observeJob: Job? = null

    init {
        refreshAds()
    }

    // Loads the test data from the network into Room. Called on start, by the refresh button in
    // the top app bar and by "try again". Every kind of error is caught on its own, there is no
    // catch (e: Exception). A CancellationException (e.g. when the screen is closed) is not caught.
    fun refreshAds() {
        observeAdsIfNeeded()
        refreshJob?.cancel()
        _uiState.update { it.copy(isLoading = true, loadError = null) }
        refreshJob = viewModelScope.launch {
            try {
                repository.refreshAdsFromNetwork()
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: IOException) {
                Log.e(TAG, "No connection while loading the test data", e)
                showError(LoadError.NoInternet)
            } catch (e: HttpException) {
                Log.e(TAG, "Server error ${e.code()} while loading the test data", e)
                showError(LoadError.Server(e.code()))
            } catch (e: SerializationException) {
                Log.e(TAG, "The test data does not match the model", e)
                showError(LoadError.InvalidData)
            } catch (e: SQLiteException) {
                Log.e(TAG, "Storing the test data failed", e)
                showError(LoadError.Database)
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
            } catch (e: SQLiteException) {
                // Saving failed: the heart stays as before and the app does not crash.
                Log.e(TAG, "Changing the favorite of ad ${ad.id} failed", e)
            }
        }
    }

    // Collects the ads from Room. Because the DAO returns a Flow, the new list arrives
    // automatically after every change of the table (refresh, delete, favorite, ...).
    // Started only once; it is restarted only if a database error stopped it.
    private fun observeAdsIfNeeded() {
        if (observeJob?.isActive == true) return
        observeJob = viewModelScope.launch {
            repository.getAllAds()
                .catch { e ->
                    // Only database errors are expected here; anything else is a bug and is thrown again.
                    if (e !is SQLiteException) throw e
                    Log.e(TAG, "Reading the ads failed", e)
                    showError(LoadError.Database)
                }
                .collect { ads -> _uiState.update { it.copy(ads = ads) } }
        }
    }

    private fun showError(error: LoadError) {
        _uiState.update { it.copy(isLoading = false, loadError = error) }
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
