package com.example.mementomori

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context

fun refreshMementoWidgets(context: Context) {
    val appWidgetManager = AppWidgetManager.getInstance(context)
    val stats = loadSavedLifeStats(context)
    val quote = getDailyMementoQuote(context)


    val weekWidgetIds = appWidgetManager.getAppWidgetIds(
        ComponentName(context, MementoWidgetProvider::class.java)
    )

    for (widgetId in weekWidgetIds) {
        updateMementoWidget(
            context = context,
            appWidgetManager = appWidgetManager,
            appWidgetId = widgetId,
            stats = stats,
            quote = quote
        )
    }

    val progressWidgetIds = appWidgetManager.getAppWidgetIds(
        ComponentName(context, MementoProgressWidgetProvider::class.java)
    )

    for (widgetId in progressWidgetIds) {
        updateMementoProgressWidget(
            context = context,
            appWidgetManager = appWidgetManager,
            appWidgetId = widgetId,
            stats = stats,
            quote = quote
        )
    }
}