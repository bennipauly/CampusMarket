package de.dhbw.campusmarket.ui.adform

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import de.dhbw.campusmarket.CampusMarketApplication
import de.dhbw.campusmarket.data.Ad
import de.dhbw.campusmarket.data.AdRepository
import de.dhbw.campusmarket.data.Category
import de.dhbw.campusmarket.data.Condition
import de.dhbw.campusmarket.logic.isValidLocation
import de.dhbw.campusmarket.logic.isValidTitle
import de.dhbw.campusmarket.logic.parsePrice
import de.dhbw.campusmarket.logic.priceToInput
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "AdFormViewModel"

// Owner of the form input. Every input goes through an on...Change function.
// adId == null: create a new ad. Otherwise: edit the ad with this id.
class AdFormViewModel(
    private val repository: AdRepository,
    private val adId: Long?
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        AdFormUiState(isEditing = adId != null, isLoading = adId != null)
    )
    val uiState: StateFlow<AdFormUiState> = _uiState.asStateFlow()

    // When editing: the ad as it is stored in Room. onSaveClick takes everything from it
    // that the form does not know: id, favorite, status "sold" and creation time.
    private var originalAd: Ad? = null

    init {
        loadAd()
    }

    // Fills the form once with the stored ad. When creating, nothing happens.
    fun loadAd() {
        if (adId == null) return
        _uiState.update { it.copy(isLoading = true, hasError = false) }
        viewModelScope.launch {
            try {
                // first() instead of collect: load only once. Otherwise every later change
                // in Room would overwrite the input the user is typing.
                val ad = repository.getAd(adId).first()
                originalAd = ad
                _uiState.update {
                    if (ad == null) {
                        // The ad was deleted in the meantime.
                        Log.w(TAG, "Ad $adId not found")
                        it.copy(isLoading = false, notFound = true)
                    } else if (ad.isTestData) {
                        // Second safety net: the detail screen shows no pencil for test ads anyway.
                        Log.w(TAG, "Ad $adId is test data and cannot be edited")
                        it.copy(isLoading = false, isReadOnly = true)
                    } else {
                        it.copy(
                            isLoading = false,
                            title = ad.title,
                            description = ad.description,
                            price = priceToInput(ad.price),
                            category = ad.category,
                            condition = ad.condition,
                            location = ad.location,
                            imageUri = ad.imageUri
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Loading ad $adId failed", e)
                _uiState.update { it.copy(isLoading = false, hasError = true) }
            }
        }
    }

    fun onTitleChange(title: String) {
        _uiState.update { it.copy(title = title) }
    }

    fun onDescriptionChange(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    fun onPriceChange(price: String) {
        _uiState.update { it.copy(price = price) }
    }

    fun onCategoryChange(category: Category) {
        _uiState.update { it.copy(category = category) }
    }

    fun onConditionChange(condition: Condition) {
        _uiState.update { it.copy(condition = condition) }
    }

    fun onLocationChange(location: String) {
        _uiState.update { it.copy(location = location) }
    }

    // The route created an empty photo file and is about to start the camera.
    fun onPhotoUriCreated(uri: String) {
        _uiState.update {
            it.copy(
                pendingPhotoUri = uri,
                showPermissionHint = false,
                showNoCameraHint = false,
                showGalleryError = false
            )
        }
    }

    // Result of the camera: success = false means cancelled, then the old photo stays.
    fun onPhotoResult(success: Boolean) {
        _uiState.update {
            val newImage = if (success) it.pendingPhotoUri else it.imageUri
            it.copy(imageUri = newImage, pendingPhotoUri = null)
        }
    }

    // A photo from the gallery was copied into the app storage. It replaces the old photo.
    fun onGalleryPhotoCopied(uri: String) {
        Log.d(TAG, "Gallery photo copied into the app storage")
        _uiState.update {
            it.copy(
                imageUri = uri,
                showPermissionHint = false,
                showNoCameraHint = false,
                showGalleryError = false
            )
        }
    }

    fun onGalleryPhotoFailed() {
        _uiState.update { it.copy(showGalleryError = true) }
    }

    fun onCameraPermissionDenied() {
        _uiState.update { it.copy(showPermissionHint = true) }
    }

    fun onCameraUnavailable() {
        _uiState.update { it.copy(showNoCameraHint = true, pendingPhotoUri = null) }
    }

    fun onRemovePhotoClick() {
        _uiState.update { it.copy(imageUri = null) }
    }

    fun onSaveClick() {
        val state = _uiState.value
        // A double click on "save" must not create two ads.
        // While the ad is still loading, there is nothing to save.
        if (state.isSaving || state.isLoading || state.isReadOnly) return

        val price = parsePrice(state.price)
        if (!isValidTitle(state.title) || price == null || !isValidLocation(state.location)) {
            // Invalid: save nothing, only show the errors below the fields.
            _uiState.update { it.copy(showErrors = true) }
            return
        }

        _uiState.update { it.copy(isSaving = true, saveFailed = false) }
        val ad = Ad(
            title = state.title.trim(),
            description = state.description.trim(),
            price = price,
            category = state.category,
            condition = state.condition,
            location = state.location.trim(),
            imageUri = state.imageUri,
            createdAt = System.currentTimeMillis()
        )
        val original = originalAd
        viewModelScope.launch {
            try {
                if (original == null) {
                    val newId = repository.addAd(ad)
                    Log.i(TAG, "Ad $newId created")
                } else {
                    // Same id = Room overwrites the row. The creation time stays,
                    // so that the ad does not jump to the top of the list.
                    repository.updateAd(
                        ad.copy(
                            id = original.id,
                            // Keeps the link to the test data, otherwise a refresh would add the ad again.
                            remoteId = original.remoteId,
                            isFavorite = original.isFavorite,
                            isSold = original.isSold,
                            createdAt = original.createdAt
                        )
                    )
                    Log.i(TAG, "Ad ${original.id} updated")
                }
                _uiState.update { it.copy(isSaving = false, isSaved = true) }
            } catch (e: Exception) {
                // Saving failed: show a hint, the input is kept.
                Log.e(TAG, "Saving the ad failed", e)
                _uiState.update { it.copy(isSaving = false, saveFailed = true) }
            }
        }
    }

    companion object {
        fun factory(adId: Long?): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as CampusMarketApplication
                AdFormViewModel(application.repository, adId)
            }
        }
    }
}
