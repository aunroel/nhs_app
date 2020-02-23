package uclsse.comp0102.nhsxapp.api.background.workers

import android.content.Context
import androidx.work.*
import uclsse.comp0102.nhsxapp.api.R
import uclsse.comp0102.nhsxapp.api.background.notification.NotificationApplier
import uclsse.comp0102.nhsxapp.api.repository.NhsFileRepository
import uclsse.comp0102.nhsxapp.api.repository.files.ModelFile
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
            val repository = NhsFileRepository(appContext)
            val tfLiteFileSubDirWithName =
                applicationContext.getString(R.string.TFL_FILE_NAME_WITH_SUB_DIR)
            val tfFileFile = repository.access(ModelFile::class.java, tfLiteFileSubDirWithName)
            val applier = NotificationApplier.getInstance(applicationContext)
            setForeground(applier.apply("Download Task"))
            return if (tfFileFile.update()) Result.success() else Result.retry()
        }
    }
}
