package de.dhbw.campusmarket.ui

import org.junit.Assert.assertEquals
import org.junit.Test

class PriceFormatTest {

    @Test
    fun formatPrice_usesCommaAndTwoDecimals() {
        assertEquals("12,50 €", formatPrice(12.5))
    }

    @Test
    fun formatPrice_wholeNumberGetsZeroDecimals() {
        assertEquals("5,00 €", formatPrice(5.0))
    }

    @Test
    fun formatPrice_roundsToTwoDecimals() {
        assertEquals("0,99 €", formatPrice(0.989))
    }
}
