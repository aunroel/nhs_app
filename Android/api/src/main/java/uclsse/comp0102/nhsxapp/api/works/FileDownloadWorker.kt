package uclsse.comp0102.nhsxapp.api.works

import android.content.Context
import androidx.work.*
import uclsse.comp0102.nhsxapp.api.R
import uclsse.comp0102.nhsxapp.api.repository.NhsRepository
import uclsse.comp0102.nhsxapp.api.repository.files.MlFile
import uclsse.comp0102.nhsxapp.api.works.notification.ForegroundNotificationApplier
import java.util.*

class FileDownloadWorker : iNhsCoroutineWorker {

    override val workName = "Download Task"
    override val workType = NhsDownloadWork::class.java
    override val workConstraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresDeviceIdle(true)
        .apply {
            val midNight = 0..7
            val weekend = listOf(Calendar.SUNDAY)
            val calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentWeekDay = calendar.get(Calendar.DAY_OF_WEEK)
            currentHour in midNight
                && currentWeekDay in weekend
        }.build()
    override val policy = ExistingPeriodicWorkPolicy.KEEP

    class NhsDownloadWork(
        private val appContext: Context, parameters: WorkerParameters
    ) : CoroutineWorker(appContext, parameters) {
        override suspend fun doWork(): Result {
            val repository = NhsRepository(appContext)
            val tfLiteFileSubDirWithName =
                applicationContext.getString(R.string.TFL_FILE_NAME_WITH_SUB_DIR)
            val tfFileFile = repository.access(MlFile::class.java, tfLiteFileSubDirWithName)
            val applier = ForegroundNotificationApplier.getInstance(applicationContext)
            setForeground(applier.apply("Download Task"))
            return if (tfFileFile.download()) Result.success() else Result.retry()
        }
    }
}
