package de.dhbw.campusmarket.ui.adlist

import de.dhbw.campusmarket.data.Ad
import de.dhbw.campusmarket.data.Category
import de.dhbw.campusmarket.logic.SortOrder
import de.dhbw.campusmarket.logic.filterAds
import de.dhbw.campusmarket.logic.searchAds
import de.dhbw.campusmarket.logic.sortAds

// Everything the list screen has to show. There are three states:
// Loading (isLoading = true), Error (hasError = true) and Ready (the list in ads).
data class AdListUiState(
    val isLoading: Boolean = true,
    val hasError: Boolean = false,
    // All ads exactly as they come from the database.
    val ads: List<Ad> = emptyList(),
    // Text in the search field.
    val query: String = "",
    // Filter chips below the search field. categoryFilter == null means: all categories.
    val categoryFilter: Category? = null,
    val onlyFavorites: Boolean = false,
    val onlyAvailable: Boolean = false,
    // Chosen in the sort menu of the top app bar.
    val sortOrder: SortOrder = SortOrder.NEWEST
) {
    // The ads that match the search and all filters, in the chosen order. Computed from the
    // fields above and not stored separately, so nothing can get out of sync.
    val visibleAds: List<Ad>
        get() = sortAds(filterAds(searchAds(ads, query), categoryFilter, onlyFavorites, onlyAvailable), sortOrder)
}
