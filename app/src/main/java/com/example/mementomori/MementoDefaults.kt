package com.example.mementomori

import java.time.LocalDate

const val DEFAULT_LIFE_EXPECTANCY_YEARS = 80
const val MIN_LIFE_EXPECTANCY_YEARS = 1
const val MAX_LIFE_EXPECTANCY_YEARS = 150
const val LIFE_CALENDAR_COLUMNS = 52
const val WEEK_WIDGET_REQUEST_CODE = 0
const val PROGRESS_WIDGET_REQUEST_CODE = 1

const val EMPTY_WIDGET_TITLE = "Configura tu vida"
const val EMPTY_WIDGET_SUBTITLE = "Abre la app para empezar."
const val EMPTY_WIDGET_QUOTE = "Esta app es tuya."

const val WIDGET_PROGRESS_MAX = 10000

val DEFAULT_BIRTH_DATE: LocalDate = LocalDate.of(1995, 1, 1)