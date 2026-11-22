package de.dhbw.campusmarket.logic

import de.dhbw.campusmarket.data.Ad
import de.dhbw.campusmarket.data.Category
import de.dhbw.campusmarket.data.Condition
import org.junit.Assert.assertEquals
import org.junit.Test

class AdSearchTest {

    // Hilfsfunktion: baut eine Anzeige, bei der nur die durchsuchten Felder wichtig sind.
    private fun ad(id: Long, title: String, description: String = "", location: String = "") = Ad(
        id = id,
        title = title,
        description = description,
        price = 1.0,
        category = Category.OTHER,
        condition = Condition.GOOD,
        location = location,
        createdAt = 0L
    )

    private val book = ad(1, "Analysis 1 – Lehrbuch", "Kaum benutzt", "Bibliothek")
    private val calculator = ad(2, "Taschenrechner TI-30X", "Für Klausuren zugelassen", "Mensa")
    private val chair = ad(3, "Schreibtischstuhl", "Nur Abholung", "Parkplatz")
    private val allAds = listOf(book, calculator, chair)

    @Test
    fun emptyQuery_returnsAllAds() {
        assertEquals(allAds, searchAds(allAds, ""))
    }

    @Test
    fun blankQuery_returnsAllAds() {
        assertEquals(allAds, searchAds(allAds, "   "))
    }

    @Test
    fun findsMatchInTitle() {
        assertEquals(listOf(calculator), searchAds(allAds, "rechner"))
    }

    @Test
    fun findsMatchInDescription() {
        assertEquals(listOf(calculator), searchAds(allAds, "Klausuren"))
    }

    @Test
    fun findsMatchInLocation() {
        assertEquals(listOf(chair), searchAds(allAds, "Parkplatz"))
    }

    @Test
    fun ignoresUpperAndLowerCase() {
        assertEquals(listOf(book), searchAds(allAds, "aNaLySiS"))
    }

    @Test
    fun ignoresSpacesAroundQuery() {
        assertEquals(listOf(book), searchAds(allAds, "  Bibliothek  "))
    }

    @Test
    fun noMatch_returnsEmptyList() {
        assertEquals(emptyList<Ad>(), searchAds(allAds, "Fahrrad"))
    }

    @Test
    fun keepsOriginalOrder() {
        // "ch" steckt in "Lehrbuch", "Taschenrechner" und "Schreibtischstuhl".
        assertEquals(allAds, searchAds(allAds, "ch"))
    }

    @Test
    fun emptyList_returnsEmptyList() {
        assertEquals(emptyList<Ad>(), searchAds(emptyList(), "Buch"))
    }
}
