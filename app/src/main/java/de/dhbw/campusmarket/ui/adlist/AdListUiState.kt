package de.dhbw.campusmarket.ui.adlist

import de.dhbw.campusmarket.data.Ad
import de.dhbw.campusmarket.data.Category
import de.dhbw.campusmarket.logic.filterAds
import de.dhbw.campusmarket.logic.searchAds

// Alles, was der Listen-Screen anzeigen muss. Es gibt drei Zustände:
// Loading (isLoading = true), Error (hasError = true) und Ready (die Liste in ads).
data class AdListUiState(
    val isLoading: Boolean = true,
    val hasError: Boolean = false,
    // Alle Anzeigen so, wie sie aus der Datenbank kommen.
    val ads: List<Ad> = emptyList(),
    // Text im Suchfeld.
    val query: String = "",
    // Filter-Chips unter dem Suchfeld. categoryFilter == null heißt: alle Kategorien.
    val categoryFilter: Category? = null,
    val onlyFavorites: Boolean = false,
    val onlyAvailable: Boolean = false
) {
    // Die Anzeigen, die zur Suche und zu allen Filtern passen. Wird aus ads, query und den
    // Filtern berechnet und nicht extra gespeichert, damit nichts auseinanderlaufen kann.
    val visibleAds: List<Ad>
        get() = filterAds(searchAds(ads, query), categoryFilter, onlyFavorites, onlyAvailable)
}
