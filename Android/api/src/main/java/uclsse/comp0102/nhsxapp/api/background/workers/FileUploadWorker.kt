package uclsse.comp0102.nhsxapp.api.background.workers

import android.content.Context
import androidx.work.*
import uclsse.comp0102.nhsxapp.api.R
import uclsse.comp0102.nhsxapp.api.background.notification.NotificationApplier
import uclsse.comp0102.nhsxapp.api.repository.NhsFileRepository
import uclsse.comp0102.nhsxapp.api.repository.files.JsonFile
import java.util.*

class FileUploadWorker : iNhsCoroutineWorker {

    private lateinit var repository: NhsFileRepository

    override val workName = "Upload Task"
    override val workType = NhsUploadWork::class.java
    override val workConstraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresDeviceIdle(true)
        .apply {
            val midNight = 0..7
            val weekend = listOf(Calendar.SATURDAY)
            val calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentWeekDay = calendar.get(Calendar.DAY_OF_WEEK)
            currentHour in midNight && currentWeekDay in weekend
        }.build()

    override val policy = ExistingPeriodicWorkPolicy.KEEP

    class NhsUploadWork(
        private val appContext: Context, parameters: WorkerParameters
    ) : CoroutineWorker(appContext, parameters) {

        override suspend fun doWork(): Result {
            val repository = NhsFileRepository(appContext)
            val jsonFileSubDirWithName =
                applicationContext.getString(R.string.JSON_FILE_NAME_WITH_SUB_DIR)
            val jsonFileFile = repository.access(JsonFile::class.java, jsonFileSubDirWithName)
            val applier = NotificationApplier.getInstance(applicationContext)
            setForeground(applier.apply("Upload Task"))
            jsonFileFile.upload()
            return Result.success()
        }

    }
}


