package com.example.mementomori

import java.time.LocalDate

data class MementoSettingsValidation(
    val lifeExpectancyYears: Int?,
    val isValidLifeExpectancy: Boolean,
    val isValidBirthDate: Boolean
) {
    val canSave: Boolean
        get() = isValidLifeExpectancy && isValidBirthDate
}

fun validateMementoSettings(
    birthDate: LocalDate,
    lifeExpectancyText: String
): MementoSettingsValidation {
    val lifeExpectancyYears = lifeExpectancyText.toIntOrNull()

    val isValidLifeExpectancy = lifeExpectancyYears != null &&
            lifeExpectancyYears in MIN_LIFE_EXPECTANCY_YEARS..MAX_LIFE_EXPECTANCY_YEARS

    val isValidBirthDate = birthDate <= LocalDate.now()

    return MementoSettingsValidation(
        lifeExpectancyYears = lifeExpectancyYears,
        isValidLifeExpectancy = isValidLifeExpectancy,
        isValidBirthDate = isValidBirthDate
    )
}