package de.dhbw.campusmarket.ui.adform

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

// Besitzer der Formulareingaben. Jede Eingabe läuft über eine on...Change-Funktion.
// adId == null: neue Anzeige erstellen. Sonst: die Anzeige mit dieser id bearbeiten.
class AdFormViewModel(
    private val repository: AdRepository,
    private val adId: Long?
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        AdFormUiState(isEditing = adId != null, isLoading = adId != null)
    )
    val uiState: StateFlow<AdFormUiState> = _uiState.asStateFlow()

    // Beim Bearbeiten die Anzeige, wie sie in Room steht. Daraus übernimmt onSaveClick alles,
    // was das Formular nicht kennt: id, Favorit, Status "verkauft" und Erstellzeitpunkt.
    private var originalAd: Ad? = null

    init {
        loadAd()
    }

    // Füllt das Formular einmal mit der gespeicherten Anzeige. Beim Erstellen passiert nichts.
    fun loadAd() {
        if (adId == null) return
        _uiState.update { it.copy(isLoading = true, hasError = false) }
        viewModelScope.launch {
            try {
                // first() statt collect: nur einmal laden. Sonst würde jede spätere Änderung
                // in Room die Eingaben überschreiben, die man gerade tippt.
                val ad = repository.getAd(adId).first()
                originalAd = ad
                _uiState.update {
                    if (ad == null) {
                        // Anzeige wurde inzwischen gelöscht.
                        it.copy(isLoading = false, notFound = true)
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
        // Solange die Anzeige noch lädt, gibt es nichts zu speichern.
        if (state.isSaving || state.isLoading) return

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
        val original = originalAd
        viewModelScope.launch {
            try {
                if (original == null) {
                    repository.addAd(ad)
                } else {
                    // Gleiche id = Room überschreibt die Zeile. Erstellzeitpunkt bleibt,
                    // damit die Anzeige in der Liste nicht nach oben springt.
                    repository.updateAd(
                        ad.copy(
                            id = original.id,
                            isFavorite = original.isFavorite,
                            isSold = original.isSold,
                            createdAt = original.createdAt
                        )
                    )
                }
                _uiState.update { it.copy(isSaving = false, isSaved = true) }
            } catch (e: Exception) {
                // Speichern hat nicht geklappt: Hinweis zeigen, die Eingaben bleiben erhalten.
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
