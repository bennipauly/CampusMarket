package de.dhbw.campusmarket.logic

import de.dhbw.campusmarket.data.Ad
import de.dhbw.campusmarket.data.Category
import de.dhbw.campusmarket.data.Condition
import org.junit.Assert.assertEquals
import org.junit.Test

class AdSortTest {

    // Helper: builds an ad where only the sorted fields matter.
    private fun ad(id: Long, title: String, price: Double, createdAt: Long) = Ad(
        id = id,
        title = title,
        description = "",
        price = price,
        category = Category.OTHER,
        condition = Condition.GOOD,
        location = "Mensa",
        createdAt = createdAt
    )

    private val oldCheap = ad(1, "Zirkel", 2.0, createdAt = 100L)
    private val newExpensive = ad(2, "analysis", 30.0, createdAt = 300L)
    private val middle = ad(3, "Äpfel-Buch", 10.0, createdAt = 200L)
    private val allAds = listOf(oldCheap, newExpensive, middle)

    @Test
    fun newest_sortsByCreatedAtDescending() {
        assertEquals(listOf(newExpensive, middle, oldCheap), sortAds(allAds, SortOrder.NEWEST))
    }

    @Test
    fun priceAscending_cheapestFirst() {
        assertEquals(listOf(oldCheap, middle, newExpensive), sortAds(allAds, SortOrder.PRICE_ASCENDING))
    }

    @Test
    fun priceDescending_mostExpensiveFirst() {
        assertEquals(listOf(newExpensive, middle, oldCheap), sortAds(allAds, SortOrder.PRICE_DESCENDING))
    }

    @Test
    fun title_ignoresCaseAndSortsUmlautsLikeGerman() {
        // "analysis" (lower case) comes first, "Äpfel" right after it and not after "Zirkel".
        assertEquals(listOf(newExpensive, middle, oldCheap), sortAds(allAds, SortOrder.TITLE))
    }

    @Test
    fun samePrice_keepsPreviousOrder() {
        val first = ad(4, "B", 5.0, createdAt = 0L)
        val second = ad(5, "A", 5.0, createdAt = 0L)
        assertEquals(listOf(first, second), sortAds(listOf(first, second), SortOrder.PRICE_ASCENDING))
    }

    @Test
    fun inputList_staysUnchanged() {
        val input = listOf(oldCheap, newExpensive, middle)
        sortAds(input, SortOrder.PRICE_DESCENDING)
        assertEquals(listOf(oldCheap, newExpensive, middle), input)
    }

    @Test
    fun emptyList_staysEmpty() {
        assertEquals(emptyList<Ad>(), sortAds(emptyList(), SortOrder.TITLE))
    }
}
