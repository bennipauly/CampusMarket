package de.dhbw.campusmarket.ui.photoviewer

import android.database.sqlite.SQLiteException
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

private const val TAG = "PhotoViewerViewModel"

// Owner of the full screen state. Gets the ad by its id and only keeps the photo Uri.
class PhotoViewerViewModel(
    private val repository: AdRepository,
    private val adId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(PhotoViewerUiState())
    val uiState: StateFlow<PhotoViewerUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadPhoto()
    }

    fun loadPhoto() {
        loadJob?.cancel()
        _uiState.update { it.copy(isLoading = true, hasError = false) }
        loadJob = viewModelScope.launch {
            repository.getAd(adId)
                .catch { e ->
                    // Only database errors are expected here; anything else is a bug and is thrown again.
                    if (e !is SQLiteException) throw e
                    Log.e(TAG, "Loading ad $adId failed", e)
                    _uiState.update { it.copy(isLoading = false, hasError = true) }
                }
                .collect { ad ->
                    // ad can be null if the ad was deleted in the meantime.
                    _uiState.update { it.copy(isLoading = false, hasError = false, imageUri = ad?.imageUri) }
                }
        }
    }

    companion object {
        fun factory(adId: Long): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as CampusMarketApplication
                PhotoViewerViewModel(application.repository, adId)
            }
        }
    }
}
