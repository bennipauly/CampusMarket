package de.dhbw.campusmarket.ui.adform

import de.dhbw.campusmarket.data.Category
import de.dhbw.campusmarket.data.Condition
import de.dhbw.campusmarket.logic.isValidLocation
import de.dhbw.campusmarket.logic.isValidTitle
import de.dhbw.campusmarket.logic.parsePrice

// All inputs of the form. Because they live in the ViewModel, they survive a rotation.
// The price stays text here, so that half-finished input like "12," stays in the field.
data class AdFormUiState(
    // Only editing loads data: Loading (isLoading), Error (hasError), not found (notFound).
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
    val notFound: Boolean = false,
    // true = edit an existing ad, false = create a new ad.
    val isEditing: Boolean = false,
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val category: Category = Category.BOOKS,
    val condition: Condition = Condition.GOOD,
    val location: String = "",
    // content:// Uri of the taken photo as text; null = no photo.
    val imageUri: String? = null,
    // Uri of the file the camera is currently writing to. Lives here and not in the composable
    // so that it is kept if the activity is recreated while the photo is being taken.
    val pendingPhotoUri: String? = null,
    // Hints if the camera permission was denied or there is no camera.
    val showPermissionHint: Boolean = false,
    val showNoCameraHint: Boolean = false,
    // Show errors only after the first click on "save", not already when opening.
    val showErrors: Boolean = false,
    val isSaving: Boolean = false,
    val saveFailed: Boolean = false,
    // true as soon as the ad is in Room. The route then navigates back.
    val isSaved: Boolean = false
) {
    val titleError: Boolean
        get() = showErrors && !isValidTitle(title)

    val priceError: Boolean
        get() = showErrors && parsePrice(price) == null

    val locationError: Boolean
        get() = showErrors && !isValidLocation(location)
}
