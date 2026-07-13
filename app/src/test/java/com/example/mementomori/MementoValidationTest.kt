package com.example.mementomori

import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate

class MementoValidationTest {

    @Test
    fun `valid inputs should result in canSave being true`() {
        val birthDate = LocalDate.now().minusYears(20) // Past date
        val result = validateMementoSettings(birthDate, "80")

        assertTrue(result.isValidLifeExpectancy)
        assertTrue(result.isValidBirthDate)
        assertEquals(80, result.lifeExpectancyYears)
        assertTrue(result.canSave)
    }

    @Test
    fun `non-numeric life expectancy should be invalid`() {
        val birthDate = LocalDate.now().minusYears(20)
        val result = validateMementoSettings(birthDate, "abc")

        assertFalse(result.isValidLifeExpectancy)
        assertNull(result.lifeExpectancyYears)
        assertFalse(result.canSave)
    }

    @Test
    fun `life expectancy below minimum should be invalid`() {
        val birthDate = LocalDate.now().minusYears(20)
        val result = validateMementoSettings(birthDate, "0")

        assertFalse(result.isValidLifeExpectancy)
        assertEquals(0, result.lifeExpectancyYears)
        assertFalse(result.canSave)
    }

    @Test
    fun `life expectancy above maximum should be invalid`() {
        val birthDate = LocalDate.now().minusYears(20)
        val result = validateMementoSettings(birthDate, "151")

        assertFalse(result.isValidLifeExpectancy)
        assertEquals(151, result.lifeExpectancyYears)
        assertFalse(result.canSave)
    }

    @Test
    fun `future birth date should be invalid`() {
        val futureDate = LocalDate.now().plusDays(1)
        val result = validateMementoSettings(futureDate, "80")

        assertFalse(result.isValidBirthDate)
        assertTrue(result.isValidLifeExpectancy)
        assertFalse(result.canSave)
    }

    @Test
    fun `birth date of today should be valid`() {
        val today = LocalDate.now()
        val result = validateMementoSettings(today, "80")

        assertTrue(result.isValidBirthDate)
        assertTrue(result.canSave)
    }
}
