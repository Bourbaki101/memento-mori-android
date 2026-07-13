package com.example.mementomori

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class LifeCalculatorTest {

    @Test
    fun calculateLifeStats_normalCase() {
        val lifeExpectancyYears = 80
        val yearsLived = 30

        val today = LocalDate.now()
        val birthDate = today.minusYears(yearsLived.toLong())

        val result = calculateLifeStats(birthDate, lifeExpectancyYears)

        val expectedEstimatedEndDate = birthDate.plusYears(lifeExpectancyYears.toLong())
        val expectedDaysLived = ChronoUnit.DAYS.between(birthDate, today)
        val expectedTotalDays = ChronoUnit.DAYS.between(birthDate, expectedEstimatedEndDate)
        val expectedWeeksLived = expectedDaysLived / 7
        val expectedTotalWeeks = expectedTotalDays / 7
        val expectedWeeksRemaining = expectedTotalWeeks - expectedWeeksLived
        val expectedPercentLived = (expectedDaysLived.toDouble() / expectedTotalDays.toDouble()) * 100

        assertEquals(expectedDaysLived, result.daysLived)
        assertEquals(expectedWeeksLived, result.weeksLived)
        assertEquals(expectedTotalDays, result.totalDays)
        assertEquals(expectedTotalWeeks, result.totalWeeks)
        assertEquals(expectedWeeksRemaining, result.weeksRemaining)
        assertEquals(expectedPercentLived, result.percentLived, 0.001)
        assertEquals(expectedEstimatedEndDate, result.estimatedEndDate)
    }

    @Test
    fun calculateLifeStats_futureBirthDate() {
        val lifeExpectancyYears = 80
        val today = LocalDate.now()
        val birthDate = today.plusDays(100)

        val result = calculateLifeStats(birthDate, lifeExpectancyYears)

        assertEquals(0L, result.daysLived)
        assertEquals(0L, result.weeksLived)
        assertEquals(0.0, result.percentLived, 0.001)

        val expectedTotalDays = ChronoUnit.DAYS.between(birthDate, birthDate.plusYears(lifeExpectancyYears.toLong()))
        val expectedTotalWeeks = expectedTotalDays / 7
        assertEquals(expectedTotalDays, result.totalDays)
        assertEquals(expectedTotalWeeks, result.totalWeeks)
        assertEquals(expectedTotalWeeks, result.weeksRemaining)
    }

    @Test
    fun calculateLifeStats_livedPastExpectancy() {
        val lifeExpectancyYears = 80
        val yearsLived = 90

        val today = LocalDate.now()
        val birthDate = today.minusYears(yearsLived.toLong())

        val result = calculateLifeStats(birthDate, lifeExpectancyYears)

        assertEquals(100.0, result.percentLived, 0.001)
        assertEquals(0L, result.weeksRemaining)

        val expectedDaysLived = ChronoUnit.DAYS.between(birthDate, today)
        val expectedWeeksLived = expectedDaysLived / 7
        assertEquals(expectedDaysLived, result.daysLived)
        assertEquals(expectedWeeksLived, result.weeksLived)
    }
}
