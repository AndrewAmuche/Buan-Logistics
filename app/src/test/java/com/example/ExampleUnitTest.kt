package com.example

import com.example.model.AppCurrency
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testPrimaryCurrencies() {
        val ngn = AppCurrency.fromCountry("Nigeria")
        assertEquals(AppCurrency.NGN, ngn)
        assertEquals("₦", ngn.symbol)
        assertTrue(ngn.isPrimary)

        val gbp = AppCurrency.fromCountry("United Kingdom")
        assertEquals(AppCurrency.GBP, gbp)
        assertEquals("£", gbp.symbol)
        assertTrue(gbp.isPrimary)
    }

    @Test
    fun testCountryCurrencyMapping() {
        assertEquals(AppCurrency.NGN, AppCurrency.fromCountry("Lagos, Nigeria"))
        assertEquals(AppCurrency.GBP, AppCurrency.fromCountry("London, UK"))
        assertEquals(AppCurrency.USD, AppCurrency.fromCountry("United States"))
        assertEquals(AppCurrency.EUR, AppCurrency.fromCountry("Germany"))
    }

    @Test
    fun testCurrencyFormatting() {
        val ngnFormatted = AppCurrency.NGN.format(100.0)
        assertTrue(ngnFormatted.startsWith("₦"))

        val gbpFormatted = AppCurrency.GBP.format(100.0)
        assertTrue(gbpFormatted.startsWith("£"))
    }
}
