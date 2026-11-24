package de.dhbw.campusmarket.ui.addetail

import android.util.Log
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

private const val TAG = "AdDetailViewModel"

// Owner of the detail state. Gets the id of the ad from the route.
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

    // Observes the one ad in Room. If it changes, the new state arrives automatically.
    fun loadAd() {
        loadJob?.cancel()
        _uiState.update { it.copy(isLoading = true, hasError = false) }
        loadJob = viewModelScope.launch {
            repository.getAd(adId)
                .catch { e ->
                    Log.e(TAG, "Loading ad $adId failed", e)
                    _uiState.update { it.copy(isLoading = false, hasError = true) }
                }
                .collect { ad ->
                    _uiState.update { it.copy(isLoading = false, hasError = false, ad = ad) }
                }
        }
    }

    // Heart at the top right: toggles the favorite. The new state arrives through the Flow from Room.
    fun onFavoriteClick() {
        val ad = _uiState.value.ad ?: return
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

    // Button below the details: switches between "available" and "sold".
    fun onSoldClick() {
        val ad = _uiState.value.ad ?: return
        viewModelScope.launch {
            try {
                repository.setSold(ad.id, !ad.isSold)
                Log.d(TAG, "Ad ${ad.id}: sold set to ${!ad.isSold}")
            } catch (e: Exception) {
                // Saving failed: the status stays as before and the app does not crash.
                Log.e(TAG, "Changing the status of ad ${ad.id} failed", e)
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
        // Stop observing first. Otherwise Room would send "null" after deleting
        // and the screen would briefly show "ad not found" before navigating back.
        loadJob?.cancel()
        viewModelScope.launch {
            try {
                repository.deleteAd(ad)
                Log.i(TAG, "Ad ${ad.id} deleted")
                _uiState.update { it.copy(isDeleted = true) }
            } catch (e: Exception) {
                // Deleting failed: show an error instead of crashing.
                Log.e(TAG, "Deleting ad ${ad.id} failed", e)
                _uiState.update { it.copy(hasError = true) }
            }
        }
    }

    companion object {
        // The id comes as a parameter, so this is a function and not a fixed object.
        fun factory(adId: Long): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as CampusMarketApplication
                AdDetailViewModel(application.repository, adId)
            }
        }
    }
}
