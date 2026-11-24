package de.dhbw.campusmarket.ui.addetail

import de.dhbw.campusmarket.data.Ad

// State of the detail screen: Loading (isLoading), Error (hasError) or Ready (ad).
// If ad == null after loading, the ad does not exist (anymore).
data class AdDetailUiState(
    val isLoading: Boolean = true,
    val hasError: Boolean = false,
    val ad: Ad? = null,
    // Lives in the ViewModel so that the open dialog survives a rotation.
    val showDeleteDialog: Boolean = false,
    // true as soon as the ad is deleted. The route then navigates back.
    val isDeleted: Boolean = false
)
