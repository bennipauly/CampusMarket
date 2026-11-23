package de.dhbw.campusmarket.ui.adform

import androidx.lifecycle.ViewModel
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Besitzer der Formulareingaben. Jede Eingabe läuft über eine on...Change-Funktion.
class AdFormViewModel(private val repository: AdRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AdFormUiState())
    val uiState: StateFlow<AdFormUiState> = _uiState.asStateFlow()

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

    // Die Route hat eine leere Foto-Datei angelegt und startet gleich die Kamera.
    fun onPhotoUriCreated(uri: String) {
        _uiState.update {
            it.copy(pendingPhotoUri = uri, showPermissionHint = false, showNoCameraHint = false)
        }
    }

    // Ergebnis der Kamera: success = false heißt abgebrochen, dann bleibt das alte Foto.
    fun onPhotoResult(success: Boolean) {
        _uiState.update {
            val newImage = if (success) it.pendingPhotoUri else it.imageUri
            it.copy(imageUri = newImage, pendingPhotoUri = null)
        }
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
        // Doppelklick auf "Speichern" soll nicht zwei Anzeigen anlegen.
        if (state.isSaving) return

        val price = parsePrice(state.price)
        if (!isValidTitle(state.title) || price == null || !isValidLocation(state.location)) {
            // Ungültig: nichts speichern, nur die Fehler unter den Feldern einblenden.
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
        viewModelScope.launch {
            try {
                repository.addAd(ad)
                _uiState.update { it.copy(isSaving = false, isSaved = true) }
            } catch (e: Exception) {
                // Speichern hat nicht geklappt: Hinweis zeigen, die Eingaben bleiben erhalten.
                _uiState.update { it.copy(isSaving = false, saveFailed = true) }
            }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as CampusMarketApplication
                AdFormViewModel(application.repository)
            }
        }
    }
}
