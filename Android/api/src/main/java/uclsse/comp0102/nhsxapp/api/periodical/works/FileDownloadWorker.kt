package uclsse.comp0102.nhsxapp.api.periodical.works

import android.content.Context
import androidx.work.*
import uclsse.comp0102.nhsxapp.api.R
import uclsse.comp0102.nhsxapp.api.periodical.notification.ForegroundNotificationApplier
import uclsse.comp0102.nhsxapp.api.repository.NhsRepository
import uclsse.comp0102.nhsxapp.api.repository.files.JsonGlobalFile
import uclsse.comp0102.nhsxapp.api.repository.files.NhsModelGlobalFile
import java.util.*

class FileDownloadWorker : iNhsCoroutineWorker {

    private var fileRepository: NhsRepository? = null

    override val workName = "Download Task"
    override val workType = NhsDownloadWork::class.java
    override val workConstraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresDeviceIdle(true)
        .apply {
            val midNight = 0..7
            val weekend = listOf(Calendar.SUNDAY, Calendar.SATURDAY)
            val calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentWeekDay = calendar.get(Calendar.DAY_OF_WEEK)
            fileRepository != null
                && fileRepository!!.isAllDataClear()
                && currentHour in midNight
                && currentWeekDay in weekend
        }.build()
    override val policy = ExistingPeriodicWorkPolicy.KEEP

    inner class NhsDownloadWork(
        appContext: Context, parameters: WorkerParameters
    ) : CoroutineWorker(appContext, parameters) {

        private val jsonDataFile: JsonGlobalFile
        private val jsonBackUpFile: JsonGlobalFile
        private val tfModelFile: NhsModelGlobalFile

        init {
            val repository = NhsRepository.getRepository()!!
            val fileSubPath = applicationContext.getString(R.string.SERVER_REPOSITORY_DIR)
            val jsonDataFileName = applicationContext.getString(R.string.JSON_DATA_FILE_NAME)
            val jsonBackupFileName = applicationContext.getString(R.string.JSON_BACKUP_FILE_NAME)
            val tfFileName = applicationContext.getString(R.string.MODEL_FILE_NAME)
            jsonDataFile = repository.access(jsonDataFileName, fileSubPath)
            jsonBackUpFile = repository.access(jsonBackupFileName, fileSubPath)
            tfModelFile = repository.access(tfFileName, fileSubPath)
        }

        override suspend fun doWork(): Result {
            val applier = ForegroundNotificationApplier.getInstance(applicationContext)
            setForeground(applier.apply(workName))
            fileRepository?.pull()
            return Result.success()
        }
    }
}
