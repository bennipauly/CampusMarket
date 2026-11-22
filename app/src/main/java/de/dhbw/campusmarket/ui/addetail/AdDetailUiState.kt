package de.dhbw.campusmarket.ui.addetail

import de.dhbw.campusmarket.data.Ad

// Zustand der Detailseite: Loading (isLoading), Error (hasError) oder Ready (ad).
// Ist nach dem Laden ad == null, gibt es die Anzeige nicht (mehr).
data class AdDetailUiState(
    val isLoading: Boolean = true,
    val hasError: Boolean = false,
    val ad: Ad? = null,
    // Liegt im ViewModel, damit der offene Dialog eine Drehung übersteht.
    val showDeleteDialog: Boolean = false,
    // true, sobald die Anzeige gelöscht ist. Die Route navigiert dann zurück.
    val isDeleted: Boolean = false
)
