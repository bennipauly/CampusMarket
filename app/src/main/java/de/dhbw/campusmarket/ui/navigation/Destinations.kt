package de.dhbw.campusmarket.ui.navigation

import kotlinx.serialization.Serializable

// Ziele der Navigation. @Serializable macht sie typsicher: Navigation Compose
// wandelt sie selbst in eine Route um, Tippfehler in Strings gibt es so nicht.

// Startseite mit allen Anzeigen. Braucht keine Parameter, darum ein object.
@Serializable
object AdList

// Detailseite. Übergeben wird nur die id, die Anzeige selbst lädt der Screen aus der Datenbank.
@Serializable
data class AdDetail(val adId: Long)

// Formular zum Erstellen einer neuen Anzeige.
@Serializable
object AdForm
