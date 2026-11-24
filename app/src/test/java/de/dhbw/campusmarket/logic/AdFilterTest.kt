package de.dhbw.campusmarket.logic

import de.dhbw.campusmarket.data.Ad
import de.dhbw.campusmarket.data.Category
import de.dhbw.campusmarket.data.Condition
import org.junit.Assert.assertEquals
import org.junit.Test

class AdFilterTest {

    // Hilfsfunktion: baut eine Anzeige, bei der nur die gefilterten Felder wichtig sind.
    private fun ad(id: Long, category: Category, isFavorite: Boolean = false, isSold: Boolean = false) = Ad(
        id = id,
        title = "Anzeige $id",
        description = "",
        price = 1.0,
        category = category,
        condition = Condition.GOOD,
        location = "Mensa",
        isFavorite = isFavorite,
        isSold = isSold,
        createdAt = 0L
    )

    private val book = ad(1, Category.BOOKS, isFavorite = true)
    private val soldBook = ad(2, Category.BOOKS, isSold = true)
    private val calculator = ad(3, Category.CALCULATORS, isFavorite = true, isSold = true)
    private val chair = ad(4, Category.FURNITURE)
    private val allAds = listOf(book, soldBook, calculator, chair)

    @Test
    fun noFilter_returnsAllAds() {
        assertEquals(allAds, filterAds(allAds, null, onlyFavorites = false, onlyAvailable = false))
    }

    @Test
    fun category_keepsOnlyThatCategory() {
        assertEquals(listOf(book, soldBook), filterAds(allAds, Category.BOOKS, onlyFavorites = false, onlyAvailable = false))
    }

    @Test
    fun onlyFavorites_keepsOnlyFavorites() {
        assertEquals(listOf(book, calculator), filterAds(allAds, null, onlyFavorites = true, onlyAvailable = false))
    }

    @Test
    fun onlyAvailable_removesSoldAds() {
        assertEquals(listOf(book, chair), filterAds(allAds, null, onlyFavorites = false, onlyAvailable = true))
    }

    @Test
    fun allFilters_mustMatchTogether() {
        assertEquals(listOf(book), filterAds(allAds, Category.BOOKS, onlyFavorites = true, onlyAvailable = true))
    }

    @Test
    fun noMatch_returnsEmptyList() {
        assertEquals(emptyList<Ad>(), filterAds(allAds, Category.TECH, onlyFavorites = false, onlyAvailable = false))
    }

    @Test
    fun emptyList_staysEmpty() {
        assertEquals(emptyList<Ad>(), filterAds(emptyList(), Category.BOOKS, onlyFavorites = true, onlyAvailable = true))
    }
}
