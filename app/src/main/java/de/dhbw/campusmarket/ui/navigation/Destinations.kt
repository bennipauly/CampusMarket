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

// Foto einer Anzeige im Vollbild. Auch hier nur die id, die Foto-Uri holt der Screen selbst.
@Serializable
data class PhotoViewer(val adId: Long)

// Formular zum Erstellen einer neuen Anzeige.
@Serializable
object AdForm

// Dasselbe Formular zum Bearbeiten einer bestehenden Anzeige. Übergeben wird nur die id.
@Serializable
data class AdEdit(val adId: Long)
