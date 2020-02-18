package uclsse.comp0102.nhsxapp.api.works

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters


abstract class AbsNhsApiCoroutineWork(
    appContext: Context,
    parameters: WorkerParameters
): CoroutineWorker(appContext, parameters) {

    protected val foreNotiApplier: ForegroundNotificationApplier =
        ForegroundNotificationApplier.getInstance(appContext)

    protected class ForegroundNotificationApplier constructor(
        private val appContext: Context
    ){
        private val foregroundNotificationsInfoList: MutableList<Notification>
        private val notificationManager: NotificationManager
        private val notificationChannel: NotificationChannel

        companion object {
            private const val CHANNEL_ID = "comp0102"
            private const val CHANNEL_NAME = "NHSX_BACKGROUND"
            private const val NOTIFICATION_TITLE = "NHSX"
            private val core = mutableMapOf<Context, ForegroundNotificationApplier>()

            fun getInstance(byContext: Context): ForegroundNotificationApplier {
                if(!core.containsKey(byContext))
                    core[byContext] =
                        ForegroundNotificationApplier(
                            byContext
                        )
                return core[byContext]!!
            }
        }

        init {
            val contextNotificationService = appContext.getSystemService(Context.NOTIFICATION_SERVICE)
            foregroundNotificationsInfoList = mutableListOf()
            notificationManager = contextNotificationService as NotificationManager
            notificationChannel =  NotificationChannel(
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
}

