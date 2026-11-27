package de.dhbw.campusmarket.data.network

import de.dhbw.campusmarket.data.Ad
import de.dhbw.campusmarket.data.Category
import de.dhbw.campusmarket.data.Condition
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AdMappingTest {

    private val dto = AdDto(
        remoteId = 7,
        title = "Zirkel",
        description = "Wie neu",
        price = 3.5,
        category = Category.OTHER,
        condition = Condition.LIKE_NEW,
        location = "Mensa",
        imageUrl = "https://example.org/zirkel.jpg"
    )

    @Test
    fun copiesAllFields() {
        val ad = toAds(listOf(dto), now = 1000L).single()
        assertEquals(7L, ad.remoteId)
        assertEquals("Zirkel", ad.title)
        assertEquals("Wie neu", ad.description)
        assertEquals(3.5, ad.price, 0.0)
        assertEquals(Category.OTHER, ad.category)
        assertEquals(Condition.LIKE_NEW, ad.condition)
        assertEquals("Mensa", ad.location)
        assertEquals("https://example.org/zirkel.jpg", ad.imageUri)
    }

    @Test
    fun adsFromJson_areTestData() {
        // Test data cannot be edited in the app.
        assertTrue(toAds(listOf(dto), now = 0L).single().isTestData)
    }

    @Test
    fun newAdsHaveNoLocalState() {
        val ad = toAds(listOf(dto), now = 1000L).single()
        // id = 0: Room assigns the local id on insert.
        assertEquals(0L, ad.id)
        assertFalse(ad.isFavorite)
        assertFalse(ad.isSold)
    }

    @Test
    fun firstAdIsNewest() {
        val ads = toAds(listOf(dto, dto.copy(remoteId = 8), dto.copy(remoteId = 9)), now = 10_000_000_000L)
        assertEquals(listOf(10_000_000_000L, 9_913_600_000L, 9_827_200_000L), ads.map { it.createdAt })
    }

    @Test
    fun emptyList_staysEmpty() {
        assertEquals(emptyList<Ad>(), toAds(emptyList(), now = 0L))
    }

    @Test
    fun ownAdsHaveNoRemoteId() {
        // Default of the model: ads created in the app are not part of the test data.
        val own = Ad(title = "T", description = "", price = 1.0, category = Category.TECH,
            condition = Condition.NEW, location = "Mensa", createdAt = 0L)
        assertNull(own.remoteId)
        assertFalse(own.isTestData)
    }
}
