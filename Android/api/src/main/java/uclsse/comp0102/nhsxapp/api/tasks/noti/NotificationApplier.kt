package uclsse.comp0102.nhsxapp.api.tasks.noti

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.ForegroundInfo

class NotificationApplier(
    private val appContext: Context
) {
    private val foregroundNotificationsInfoList: MutableList<Notification>
    private val notificationManager: NotificationManager
    private val notificationChannel: NotificationChannel

    companion object {
        private const val CHANNEL_ID = "comp0102"
        private const val CHANNEL_NAME = "NHSX_BACKGROUND"
        private const val NOTIFICATION_TITLE = "NHSX"
        private val core = mutableMapOf<Context, NotificationApplier>()

        fun getInstance(byContext: Context): NotificationApplier {
            if (!core.containsKey(byContext))
                core[byContext] =
                    NotificationApplier(
                        byContext
                    )
            return core[byContext]!!
        }
    }

    init {
        val contextNotificationService = appContext.getSystemService(Context.NOTIFICATION_SERVICE)
        foregroundNotificationsInfoList = mutableListOf()
        notificationManager = contextNotificationService as NotificationManager
        notificationChannel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
        )
    }

    fun apply(progress: String): ForegroundInfo {
        val notification = NotificationCompat.Builder(appContext,
            CHANNEL_ID
        )
            .setAutoCancel(true)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText(progress)
            .setWhen(System.currentTimeMillis())
            .build()
        val notificationID = foregroundNotificationsInfoList.size
        foregroundNotificationsInfoList.add(notification)
        return ForegroundInfo(notificationID, notification)
    }
}