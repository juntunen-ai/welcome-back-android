package ai.juntunen.welcomeback.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import ai.juntunen.welcomeback.MainActivity
import ai.juntunen.welcomeback.R

/**
 * Home-screen widget that shows the user's name and taps through to MainActivity.
 */
class WelcomeBackWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val prefs = context.getSharedPreferences("welcomeback_widget", Context.MODE_PRIVATE)
        val userName = prefs.getString("user_name", "") ?: ""

        for (id in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_welcome_back)
            views.setTextViewText(R.id.widget_name, userName.ifBlank { "Welcome Back" })

            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

            appWidgetManager.updateAppWidget(id, views)
        }
    }
}
