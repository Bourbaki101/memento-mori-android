package com.example.mementomori

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews


class MementoWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val stats = loadSavedLifeStats(context)
        val quote = getDailyMementoQuote(context)

        for (appWidgetId in appWidgetIds) {
            updateMementoWidget(context, appWidgetManager, appWidgetId, stats, quote)
        }
    }
}



fun updateMementoWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
    stats: LifeStats?,
    quote: String
) {
    val views = RemoteViews(context.packageName, R.layout.memento_widget)

    setupWidgetOpenAppClick(
        context = context,
        views = views,
        rootViewId = R.id.widget_root,
        requestCode = WEEK_WIDGET_REQUEST_CODE
    )

    views.setTextViewText(R.id.widget_quote, quote)



    if (stats != null) {
        views.setTextViewText(
            R.id.widget_title,
            "Semana ${formatWholeNumber(stats.weeksLived)} de ${formatWholeNumber(stats.totalWeeks)}"
        )

        views.setTextViewText(
            R.id.widget_subtitle,
            "${formatPercentage(stats.percentLived)} vivido"
        )
    } else {
        setEmptyWeekWidgetState(views)
    }

    appWidgetManager.updateAppWidget(appWidgetId, views)
}
