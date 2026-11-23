package de.dhbw.campusmarket.ui.photoviewer

// Zustand der Vollbild-Ansicht: Loading (isLoading), Error (hasError) oder Ready (imageUri).
// Ist nach dem Laden imageUri == null, hat die Anzeige kein Foto (mehr).
data class PhotoViewerUiState(
    val isLoading: Boolean = true,
    val hasError: Boolean = false,
    val imageUri: String? = null
)
