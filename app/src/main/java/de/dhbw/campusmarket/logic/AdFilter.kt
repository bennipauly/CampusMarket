package de.dhbw.campusmarket.logic

import de.dhbw.campusmarket.data.Ad
import de.dhbw.campusmarket.data.Category

// Pure Kotlin function without Android dependencies, so it can be tested with JUnit.
// Returns only the ads that match all active filters:
// - category: only this category; null = all categories
// - onlyFavorites: only ads that are marked as favorite
// - onlyAvailable: no sold ads
// If no filter is active, the whole list is returned.
fun filterAds(
    ads: List<Ad>,
    category: Category?,
    onlyFavorites: Boolean,
    onlyAvailable: Boolean
): List<Ad> {
    return ads.filter { ad ->
        (category == null || ad.category == category) &&
            (!onlyFavorites || ad.isFavorite) &&
            (!onlyAvailable || !ad.isSold)
    }
}
