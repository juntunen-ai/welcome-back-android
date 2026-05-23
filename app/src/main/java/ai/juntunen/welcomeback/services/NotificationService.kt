package ai.juntunen.welcomeback.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.*
import ai.juntunen.welcomeback.data.NotificationTime
import ai.juntunen.welcomeback.data.UserProfile
import java.util.Calendar
import java.util.concurrent.TimeUnit

private const val CHANNEL_ID   = "welcomeback_reminders"
private const val CHANNEL_NAME = "Daily Reminders"
private const val WORK_TAG     = "wb_reminder"

/**
 * Schedules daily reminder notifications via WorkManager.
 * Mirrors iOS `NotificationService` — one periodic task per active time slot.
 */
object NotificationService {

    fun createChannel(context: Context) {
        val mgr = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Gentle daily reminders from Welcome Back"
        }
        mgr.createNotificationChannel(channel)
    }

    fun scheduleReminders(context: Context, profile: UserProfile) {
        cancelAll(context)
        if (!profile.notificationsEnabled) return

        profile.notificationTimes.forEach { time ->
            val delay = initialDelayFor(time.hour, time.minute)
            val workName = "$WORK_TAG.${time.name}"

            val data = workDataOf(
                "userName"      to profile.name,
                "familyMember"  to profile.familyMembers.firstOrNull()?.name,
                "notifId"       to time.ordinal
            )

            val request = PeriodicWorkRequestBuilder<ReminderWorker>(1, TimeUnit.DAYS)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(data)
                .addTag(WORK_TAG)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                workName,
                ExistingPeriodicWorkPolicy.REPLACE,
                request
            )
        }
    }

    fun cancelAll(context: Context) {
        WorkManager.getInstance(context).cancelAllWorkByTag(WORK_TAG)
    }

    private fun initialDelayFor(hour: Int, minute: Int): Long {
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        if (target.timeInMillis <= now.timeInMillis) {
            target.add(Calendar.DAY_OF_YEAR, 1)
        }
        return target.timeInMillis - now.timeInMillis
    }
}

class ReminderWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {

    companion object {
        private val MESSAGES = listOf(
            "Good morning, %s! Take a moment to look through your memories today.",
            "Hello, %s! Your family loves you. Tap here to hear a story.",
            "Hi %s! It's a great time to listen to your favourite music.",
            "Remembering together, %s. Tap to open Welcome Back.",
            "%s is thinking of you. Open Welcome Back to connect.",
            "Time to revisit a special memory, %s.",
            "Welcome Back is here for you, %s. Come say hello.",
            "Good day, %s! Take a moment for yourself today."
        )
    }

    override suspend fun doWork(): Result {
        val userName     = inputData.getString("userName") ?: "friend"
        val familyMember = inputData.getString("familyMember")
        val notifId      = inputData.getInt("notifId", 0)

        val rawMessage = MESSAGES[notifId % MESSAGES.size]
        val nameInMsg  = familyMember ?: userName
        val message    = rawMessage.format(nameInMsg)

        val mgr = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Welcome Back")
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        mgr.notify(notifId, notification)
        return Result.success()
    }
}
