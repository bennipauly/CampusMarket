package de.dhbw.campusmarket.ui.adform

import de.dhbw.campusmarket.data.Category
import de.dhbw.campusmarket.data.Condition
import de.dhbw.campusmarket.logic.isValidLocation
import de.dhbw.campusmarket.logic.isValidTitle
import de.dhbw.campusmarket.logic.parsePrice

// Alle Eingaben des Formulars. Weil sie im ViewModel liegen, überleben sie eine Drehung.
// Der Preis bleibt hier Text, damit auch halbe Eingaben wie "12," im Feld stehen bleiben.
data class AdFormUiState(
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val category: Category = Category.BOOKS,
    val condition: Condition = Condition.GOOD,
    val location: String = "",
    // Fehler erst nach dem ersten Klick auf "Speichern" zeigen, nicht schon beim Öffnen.
    val showErrors: Boolean = false,
    val isSaving: Boolean = false,
    val saveFailed: Boolean = false,
    // true, sobald die Anzeige in Room steht. Die Route navigiert dann zurück.
    val isSaved: Boolean = false
) {
    val titleError: Boolean
        get() = showErrors && !isValidTitle(title)

    val priceError: Boolean
        get() = showErrors && parsePrice(price) == null

    val locationError: Boolean
        get() = showErrors && !isValidLocation(location)
}
