package de.dhbw.campusmarket.logic

import de.dhbw.campusmarket.data.Ad
import java.text.Collator
import java.util.Locale

// The ways the ad list can be sorted. NEWEST is the default.
enum class SortOrder {
    NEWEST,
    PRICE_ASCENDING,
    PRICE_DESCENDING,
    TITLE
}

// Pure Kotlin function without Android dependencies, so it can be tested with JUnit.
// Returns a new sorted list; the input list stays unchanged.
// The sort is stable: ads with the same price keep their previous order.
fun sortAds(ads: List<Ad>, order: SortOrder): List<Ad> {
    return when (order) {
        SortOrder.NEWEST -> ads.sortedByDescending { it.createdAt }
        SortOrder.PRICE_ASCENDING -> ads.sortedBy { it.price }
        SortOrder.PRICE_DESCENDING -> ads.sortedByDescending { it.price }
        // A German Collator sorts "Ä" next to "A" and ignores upper/lower case.
        // A plain String comparison would put umlauts after "Z".
        SortOrder.TITLE -> ads.sortedWith(compareBy(Collator.getInstance(Locale.GERMANY)) { it.title })
    }
}
