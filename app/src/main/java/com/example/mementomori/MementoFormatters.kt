package com.example.mementomori

import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatWholeNumber(value: Long): String {
    return NumberFormat.getIntegerInstance(Locale.getDefault()).format(value)
}

fun formatPercentage(value: Double): String {
    return String.format(Locale.getDefault(), "%.2f%%", value)
}

fun formatDisplayDate(date: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.getDefault())
    return date.format(formatter)
}