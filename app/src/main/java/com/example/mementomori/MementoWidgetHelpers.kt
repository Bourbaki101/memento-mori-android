package com.example.mementomori

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

fun createOpenAppPendingIntent(
    context: Context,
    requestCode: Int
): PendingIntent {
    val intent = Intent(context, MainActivity::class.java)

    return PendingIntent.getActivity(
        context,
        requestCode,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
}

fun setupWidgetOpenAppClick(
    context: Context,
    views: RemoteViews,
    rootViewId: Int,
    requestCode: Int
) {
    views.setOnClickPendingIntent(
        rootViewId,
        createOpenAppPendingIntent(
            context = context,
            requestCode = requestCode
        )
    )
}
fun setEmptyWeekWidgetState(views: RemoteViews) {
    views.setTextViewText(
        R.id.widget_title,
        EMPTY_WIDGET_TITLE
    )

    views.setTextViewText(
        R.id.widget_subtitle,
        EMPTY_WIDGET_SUBTITLE
    )

    views.setTextViewText(
        R.id.widget_quote,
        EMPTY_WIDGET_QUOTE
    )
}

fun setEmptyProgressWidgetState(views: RemoteViews) {
    views.setTextViewText(
        R.id.progress_widget_title,
        EMPTY_WIDGET_TITLE
    )

    views.setProgressBar(
        R.id.progress_widget_bar,
        WIDGET_PROGRESS_MAX,
        0,
        false
    )

    views.setTextViewText(
        R.id.progress_widget_subtitle,
        EMPTY_WIDGET_SUBTITLE
    )

    views.setTextViewText(
        R.id.progress_widget_quote,
        EMPTY_WIDGET_QUOTE
    )
}