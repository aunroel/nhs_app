package uclsse.comp0102.nhsx.android.works

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.*
import java.util.*
import java.util.concurrent.TimeUnit

class NhsUploadWork(
    private val appContext: Context,
    private val parameters: WorkerParameters
): AbsNhsApiCoroutineWorker(appContext, parameters) {

    companion object {
        var isUpload = false
        private const val WORK_NAME = "Upload Task"
        private val WORK_CONSTRAINTS = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresDeviceIdle(true)
            .apply {
                val midNight = 0..7
                val weekend = listOf(Calendar.SUNDAY, Calendar.SATURDAY)
                val calendar = Calendar.getInstance()
                val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
                val currentWeekDay =  calendar.get(Calendar.DAY_OF_WEEK)
                currentHour in midNight && currentWeekDay in weekend
            }.build()
    }

    override suspend fun doWork(): Result {
        setForeground(foreNotiApplier.apply(WORK_NAME))
        android.os.SystemClock.sleep(5000)
        isUpload = true
        return Result.success()
    }

    override fun createPeriodicWorkRequest(): PeriodicWorkRequest {
        return PeriodicWorkRequestBuilder<NhsDownloadWork>(7, TimeUnit.DAYS)
            .setConstraints(WORK_CONSTRAINTS)
            .build()
    }
}
