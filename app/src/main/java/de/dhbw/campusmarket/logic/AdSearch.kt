package de.dhbw.campusmarket.logic

import de.dhbw.campusmarket.data.Ad

// Pure Kotlin function without Android dependencies, so it can be tested with JUnit.
// Returns all ads whose title, description or handover location contains the search text.
// Upper and lower case do not matter. An empty search text returns the whole list.
fun searchAds(ads: List<Ad>, query: String): List<Ad> {
    val searchText = query.trim()
    if (searchText.isEmpty()) {
        return ads
    }
    return ads.filter { ad ->
        ad.title.contains(searchText, ignoreCase = true) ||
            ad.description.contains(searchText, ignoreCase = true) ||
            ad.location.contains(searchText, ignoreCase = true)
    }
}
