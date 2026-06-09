package com.example.mementomori

import android.content.Context

fun loadSavedLifeStats(context: Context): LifeStats? {
    val settings = loadMementoSettings(context)

    if (!settings.hasSavedData) {
        return null
    }

    return calculateLifeStats(
        birthDate = settings.birthDate,
        lifeExpectancyYears = settings.lifeExpectancyYears
    )
}