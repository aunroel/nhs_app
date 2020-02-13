package uclsse.comp0102.nhsx.android.works

import android.content.Context
import androidx.work.*
import java.util.*
import java.util.concurrent.TimeUnit

class NhsUploadWork(
    appContext: Context, parameters: WorkerParameters
): AbsNhsApiCoroutineWork(appContext, parameters) {

    companion object {
        var isUpload = false
        const val WORK_NAME = "Upload Task"
        val WORK_CONSTRAINTS = Constraints.Builder()
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

        isUpload = true
        return Result.success()
    }

}
