package de.dhbw.campusmarket.logic

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AdValidationTest {

    @Test
    fun title_normalText_isValid() {
        assertTrue(isValidTitle("Taschenrechner"))
    }

    @Test
    fun title_emptyOrBlank_isInvalid() {
        assertFalse(isValidTitle(""))
        assertFalse(isValidTitle("   "))
    }

    @Test
    fun title_exactlyMaxLength_isValid() {
        assertTrue(isValidTitle("a".repeat(MAX_TITLE_LENGTH)))
    }

    @Test
    fun title_tooLong_isInvalid() {
        assertFalse(isValidTitle("a".repeat(MAX_TITLE_LENGTH + 1)))
    }

    @Test
    fun title_spacesAroundDoNotCount() {
        assertTrue(isValidTitle("  " + "a".repeat(MAX_TITLE_LENGTH) + "  "))
    }

    @Test
    fun location_emptyOrBlank_isInvalid() {
        assertFalse(isValidLocation(""))
        assertFalse(isValidLocation("  "))
        assertTrue(isValidLocation("Mensa"))
    }

    @Test
    fun price_wholeNumber() {
        assertEquals(12.0, parsePrice("12")!!, 0.0)
    }

    @Test
    fun price_withCommaOrDot() {
        assertEquals(12.5, parsePrice("12,5")!!, 0.0)
        assertEquals(12.5, parsePrice("12.50")!!, 0.0)
    }

    @Test
    fun price_zeroIsAllowed() {
        assertEquals(0.0, parsePrice("0")!!, 0.0)
    }

    @Test
    fun price_spacesAroundAreIgnored() {
        assertEquals(7.0, parsePrice(" 7 ")!!, 0.0)
    }

    @Test
    fun price_invalidInputs_returnNull() {
        val invalid = listOf("", " ", "abc", "-5", "1e3", "NaN", "12,505", "123456", "12,", ",5", "1.000,00")
        for (input in invalid) {
            assertNull("\"$input\" sollte ungültig sein", parsePrice(input))
        }
    }

    @Test
    fun priceToInput_usesCommaAndTwoDecimals() {
        assertEquals("12,50", priceToInput(12.5))
        assertEquals("0,00", priceToInput(0.0))
    }

    @Test
    fun priceToInput_canBeParsedAgain() {
        for (price in listOf(0.0, 3.0, 12.5, 99999.99)) {
            assertEquals(price, parsePrice(priceToInput(price))!!, 0.0)
        }
    }
}
