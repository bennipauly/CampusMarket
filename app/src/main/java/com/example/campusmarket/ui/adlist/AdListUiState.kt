package com.example.campusmarket.ui.adlist

import com.example.campusmarket.data.Ad
import com.example.campusmarket.logic.searchAds

// Alles, was der Listen-Screen anzeigen muss. Es gibt drei Zustände:
// Loading (isLoading = true), Error (hasError = true) und Ready (die Liste in ads).
data class AdListUiState(
    val isLoading: Boolean = true,
    val hasError: Boolean = false,
    // Alle Anzeigen so, wie sie aus der Datenbank kommen.
    val ads: List<Ad> = emptyList(),
    // Text im Suchfeld.
    val query: String = ""
) {
    // Die Anzeigen, die zur Suche passen. Wird aus ads und query berechnet und nicht
    // extra gespeichert, damit beide Listen nie auseinanderlaufen können.
    val visibleAds: List<Ad>
        get() = searchAds(ads, query)
}
