package uclsse.comp0102.nhsxapp.api.periodical.works

import android.content.Context
import androidx.work.*
import uclsse.comp0102.nhsxapp.api.periodical.notification.ForegroundNotificationApplier
import uclsse.comp0102.nhsxapp.api.repository.NhsRepository
import java.util.*

class FileUploadWorker : iNhsCoroutineWorker {

    private lateinit var repository: NhsRepository

    override val workName = "Upload Task"
    override val workType = NhsUploadWork::class.java
    override val workConstraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresDeviceIdle(true)
        .apply {
            val midNight = 0..7
            val weekend = listOf(Calendar.SUNDAY, Calendar.SATURDAY)
            val calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentWeekDay = calendar.get(Calendar.DAY_OF_WEEK)
            currentHour in midNight && currentWeekDay in weekend
        }.build()

    override val policy = ExistingPeriodicWorkPolicy.KEEP

    inner class NhsUploadWork(
        appContext: Context, parameters: WorkerParameters
    ) : CoroutineWorker(appContext, parameters) {

        init {
            repository = NhsRepository.getRepository()!!
        }

        override suspend fun doWork(): Result {
            val applier = ForegroundNotificationApplier.getInstance(applicationContext)
            setForeground(applier.apply(workName))
            repository.push()
            return Result.success()
        }

    }
}


