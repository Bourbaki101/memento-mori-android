package com.example.mementomori

import android.content.Context
import java.time.LocalDate

data class MementoSettings(
    val birthDate: LocalDate,
    val lifeExpectancyYears: Int,
    val hasSavedData: Boolean,
    val hasSeenIntro: Boolean
)

private const val PREFS_NAME = "memento_mori_prefs"
private const val KEY_BIRTH_DATE = "birth_date"
private const val KEY_LIFE_EXPECTANCY = "life_expectancy"
private const val KEY_HAS_SAVED_DATA = "has_saved_data"
private const val KEY_HAS_SEEN_INTRO = "has_seen_intro"

fun loadMementoSettings(context: Context): MementoSettings {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val hasSeenIntro = prefs.getBoolean(KEY_HAS_SEEN_INTRO, false)
    val birthDateText = prefs.getString(KEY_BIRTH_DATE, null)

    val birthDate = birthDateText?.let {
        runCatching { LocalDate.parse(it) }.getOrNull()
    } ?: DEFAULT_BIRTH_DATE

    val lifeExpectancyYears = prefs.getInt(KEY_LIFE_EXPECTANCY, DEFAULT_LIFE_EXPECTANCY_YEARS)
    val hasSavedData = prefs.getBoolean(KEY_HAS_SAVED_DATA, false)

    return MementoSettings(
        birthDate = birthDate,
        lifeExpectancyYears = lifeExpectancyYears,
        hasSavedData = hasSavedData,
        hasSeenIntro = hasSeenIntro
    )
}

fun saveMementoSettings(
    context: Context,
    birthDate: LocalDate,
    lifeExpectancyYears: Int
) {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    prefs.edit()
        .putString(KEY_BIRTH_DATE, birthDate.toString())
        .putInt(KEY_LIFE_EXPECTANCY, lifeExpectancyYears)
        .putBoolean(KEY_HAS_SAVED_DATA, true)
        .apply()
}

fun clearMementoSettings(context: Context) {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    prefs.edit()
        .clear()
        .apply()
}
fun markIntroAsSeen(context: Context) {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    prefs.edit()
        .putBoolean(KEY_HAS_SEEN_INTRO, true)
        .apply()
}

private const val KEY_CUSTOM_QUOTES = "custom_quotes"

fun loadCustomQuotes(context: Context): List<String> {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    return prefs.getStringSet(KEY_CUSTOM_QUOTES, emptySet())
        ?.toList()
        ?.sorted()
        ?: emptyList()
}

fun saveCustomQuotes(
    context: Context,
    quotes: List<String>
) {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    prefs.edit()
        .putStringSet(KEY_CUSTOM_QUOTES, quotes.toSet())
        .apply()
}

fun addCustomQuote(
    context: Context,
    quote: String
): List<String> {
    val cleanQuote = quote.trim()

    if (cleanQuote.isBlank()) {
        return loadCustomQuotes(context)
    }

    val currentQuotes = loadCustomQuotes(context)

    val updatedQuotes = (currentQuotes + cleanQuote)
        .distinct()
        .sorted()

    saveCustomQuotes(context, updatedQuotes)

    return updatedQuotes
}

fun deleteCustomQuote(
    context: Context,
    quote: String
): List<String> {
    val updatedQuotes = loadCustomQuotes(context)
        .filterNot { it == quote }
        .sorted()

    saveCustomQuotes(context, updatedQuotes)

    return updatedQuotes
}