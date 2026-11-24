package de.dhbw.campusmarket.ui.photoviewer

// State of the full screen view: Loading (isLoading), Error (hasError) or Ready (imageUri).
// If imageUri == null after loading, the ad has no photo (anymore).
data class PhotoViewerUiState(
    val isLoading: Boolean = true,
    val hasError: Boolean = false,
    val imageUri: String? = null
)
