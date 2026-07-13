package com.example.mementomori

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Locale

class MementoFormattersTest {

    private var defaultLocale: Locale? = null

    @Before
    fun setUp() {
        defaultLocale = Locale.getDefault()
        Locale.setDefault(Locale.US)
    }

    @After
    fun tearDown() {
        defaultLocale?.let { Locale.setDefault(it) }
    }

    @Test
    fun formatWholeNumber_zero() {
        assertEquals("0", formatWholeNumber(0L))
    }

    @Test
    fun formatWholeNumber_positive_small() {
        assertEquals("123", formatWholeNumber(123L))
    }

    @Test
    fun formatWholeNumber_positive_large() {
        assertEquals("1,234,567", formatWholeNumber(1234567L))
    }

    @Test
    fun formatWholeNumber_negative_small() {
        assertEquals("-123", formatWholeNumber(-123L))
    }

    @Test
    fun formatWholeNumber_negative_large() {
        assertEquals("-1,234,567", formatWholeNumber(-1234567L))
    }

    @Test
    fun formatWholeNumber_max_value() {
        assertEquals("9,223,372,036,854,775,807", formatWholeNumber(Long.MAX_VALUE))
    }

    @Test
    fun formatWholeNumber_min_value() {
        assertEquals("-9,223,372,036,854,775,808", formatWholeNumber(Long.MIN_VALUE))
    }
}
