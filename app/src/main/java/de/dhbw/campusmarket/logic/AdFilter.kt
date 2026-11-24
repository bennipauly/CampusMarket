package de.dhbw.campusmarket.logic

import de.dhbw.campusmarket.data.Ad
import de.dhbw.campusmarket.data.Category

// Reine Kotlin-Funktion ohne Android-Abhängigkeit, darum mit JUnit testbar.
// Liefert nur die Anzeigen, die zu allen aktiven Filtern passen:
// - category: nur diese Kategorie; null = alle Kategorien
// - onlyFavorites: nur Anzeigen, die als Favorit gemerkt sind
// - onlyAvailable: keine verkauften Anzeigen
// Ist kein Filter aktiv, kommt die ganze Liste zurück.
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
