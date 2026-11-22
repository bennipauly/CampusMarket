package de.dhbw.campusmarket.logic

import de.dhbw.campusmarket.data.Ad

// Reine Kotlin-Funktion ohne Android-Abhängigkeit, darum mit JUnit testbar.
// Liefert alle Anzeigen, in deren Titel, Beschreibung oder Übergabeort der Suchtext vorkommt.
// Groß-/Kleinschreibung ist egal. Ein leerer Suchtext liefert die ganze Liste.
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
