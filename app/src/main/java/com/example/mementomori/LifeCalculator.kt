package com.example.mementomori

import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class LifeStats(
    val daysLived: Long,
    val weeksLived: Long,
    val totalDays: Long,
    val totalWeeks: Long,
    val weeksRemaining: Long,
    val percentLived: Double,
    val estimatedEndDate: LocalDate
)

fun calculateLifeStats(
    birthDate: LocalDate,
    lifeExpectancyYears: Int
): LifeStats {
    val today = LocalDate.now()
    val estimatedEndDate = birthDate.plusYears(lifeExpectancyYears.toLong())

    val daysLived = ChronoUnit.DAYS.between(birthDate, today).coerceAtLeast(0)
    val totalDays = ChronoUnit.DAYS.between(birthDate, estimatedEndDate).coerceAtLeast(1)

    val weeksLived = daysLived / 7
    val totalWeeks = totalDays / 7
    val weeksRemaining = (totalWeeks - weeksLived).coerceAtLeast(0)

    val percentLived = (daysLived.toDouble() / totalDays.toDouble()) * 100

    return LifeStats(
        daysLived = daysLived,
        weeksLived = weeksLived,
        totalDays = totalDays,
        totalWeeks = totalWeeks,
        weeksRemaining = weeksRemaining,
        percentLived = percentLived.coerceAtMost(100.0),
        estimatedEndDate = estimatedEndDate
    )
}