package de.dhbw.campusmarket.data.network

import de.dhbw.campusmarket.data.Category
import de.dhbw.campusmarket.data.Condition
import kotlinx.serialization.SerializationException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class AdDtoTest {

    // The real file from the repository. Gradle runs the tests inside the "app" folder,
    // so the project root is one level up.
    private val testDataFile = File("../testdata/anzeigen.json")

    private fun parse(json: String): List<AdDto> = adJson.decodeFromString(json)

    @Test
    fun testDataFile_matchesTheModel() {
        val ads = parse(testDataFile.readText())
        assertEquals(6, ads.size)
        assertEquals("Analysis 1 – Lehrbuch", ads[0].title)
        assertEquals(Category.BOOKS, ads[0].category)
        assertEquals(Condition.LIKE_NEW, ads[0].condition)
        assertEquals(15.0, ads[0].price, 0.0)
    }

    @Test
    fun testDataFile_hasUniqueIds() {
        val ads = parse(testDataFile.readText())
        assertEquals(ads.size, ads.map { it.remoteId }.toSet().size)
    }

    @Test
    fun testDataFile_imagesPointToTheRepository() {
        val ads = parse(testDataFile.readText())
        for (ad in ads) {
            assertTrue(ad.imageUrl, ad.imageUrl.startsWith(BASE_URL + "testdata/images/"))
        }
    }

    @Test
    fun unknownKey_isIgnored() {
        val json = """[{"id":1,"titel":"T","beschreibung":"","preis":1.0,"kategorie":"TECH",
            "zustand":"NEW","ort":"Mensa","bildUrl":"https://x","neuesFeld":true}]"""
        assertEquals(1, parse(json).size)
    }

    @Test(expected = SerializationException::class)
    fun unknownCategory_throwsSerializationException() {
        parse("""[{"id":1,"titel":"T","beschreibung":"","preis":1.0,"kategorie":"CARS",
            "zustand":"NEW","ort":"Mensa","bildUrl":"https://x"}]""")
    }

    @Test(expected = SerializationException::class)
    fun missingField_throwsSerializationException() {
        parse("""[{"id":1,"titel":"T","preis":1.0,"kategorie":"TECH","zustand":"NEW","ort":"Mensa"}]""")
    }

    @Test(expected = SerializationException::class)
    fun notJson_throwsSerializationException() {
        parse("<html>404: Not Found</html>")
    }
}
