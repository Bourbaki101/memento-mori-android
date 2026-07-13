package com.example.mementomori

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews

class MementoProgressWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val stats = loadSavedLifeStats(context)
        val quote = getDailyMementoQuote(context)
        for (appWidgetId in appWidgetIds) {
            updateMementoProgressWidget(context, appWidgetManager, appWidgetId, stats, quote)
        }
    }
}

fun updateMementoProgressWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
    stats: LifeStats? = loadSavedLifeStats(context),
    quote: String = getDailyMementoQuote(context)
) {
    val views = RemoteViews(context.packageName, R.layout.memento_progress_widget)

    setupWidgetOpenAppClick(
        context = context,
        views = views,
        rootViewId = R.id.progress_widget_root,
        requestCode = PROGRESS_WIDGET_REQUEST_CODE
    )

    if (stats != null) {
        val progressValue = (stats.percentLived * 100).toInt().coerceIn(0, WIDGET_PROGRESS_MAX)

        views.setTextViewText(
            R.id.progress_widget_title,
            "Vida completada"
        )

        views.setProgressBar(
            R.id.progress_widget_bar,
            WIDGET_PROGRESS_MAX,
            progressValue,
            false
        )

        views.setTextViewText(
            R.id.progress_widget_subtitle,
            "${formatPercentage(stats.percentLived)} vivido"
        )

        views.setTextViewText(
            R.id.progress_widget_quote,
            quote
        )
    } else {
        setEmptyProgressWidgetState(views)
    }

    appWidgetManager.updateAppWidget(appWidgetId, views)
}