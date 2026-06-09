package com.example.mementomori

import android.content.Context
import java.time.LocalDate

val DEFAULT_MEMENTO_QUOTES = listOf(
    "Aún estás aquí.",
    "Carpe diem.",
    "Recuerda que el tiempo es finito.",
    "Haz que esta semana cuente.",
    "No pospongas vivir.",
    "Un día menos, una razón más.",
    "Vive con intención.",
    "El tiempo también es vida.",
    "Hoy también importa.",
    "Memento vivere."
)

fun getDailyMementoQuote(customQuotes: List<String> = emptyList()): String {
    val allQuotes = DEFAULT_MEMENTO_QUOTES + customQuotes

    if (allQuotes.isEmpty()) {
        return "Aún estás aquí."
    }

    val dayOfYear = LocalDate.now().dayOfYear
    return allQuotes[dayOfYear % allQuotes.size]
}

fun getDailyMementoQuote(context: Context): String {
    val customQuotes = loadCustomQuotes(context)
    return getDailyMementoQuote(customQuotes)
}