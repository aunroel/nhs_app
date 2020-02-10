package uclsse.comp0102.nhsx.android.works

import android.content.Context
import androidx.work.*
import java.util.*
import java.util.concurrent.TimeUnit



class NhsDownloadWork(
    private val appContext: Context,
    private val parameters: WorkerParameters
): AbsNhsApiCoroutineWorker(appContext, parameters) {

    companion object {
        private const val WORK_NAME = "Download Task"
        private val WORK_CONSTRAINTS = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresDeviceIdle(true)
            .apply {
                val midNight = 0..7
                val weekend = listOf(Calendar.SUNDAY, Calendar.SATURDAY)
                val calendar = Calendar.getInstance()
                val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
                val currentWeekDay =  calendar.get(Calendar.DAY_OF_WEEK)
                NhsUploadWork.isUpload
                        && currentHour in midNight
                        && currentWeekDay in weekend
            }.build()
    }

    override suspend fun doWork(): Result {
        setForeground(foreNotiApplier.apply(WORK_NAME))
        android.os.SystemClock.sleep(5000)
        NhsUploadWork.isUpload = false
        return Result.success()
    }

    override fun createPeriodicWorkRequest(): PeriodicWorkRequest {
        return PeriodicWorkRequestBuilder<NhsDownloadWork>(7, TimeUnit.DAYS)
            .setConstraints(WORK_CONSTRAINTS)
            .build()
    }

}