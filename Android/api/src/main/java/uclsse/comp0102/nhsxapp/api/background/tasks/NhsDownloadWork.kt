package uclsse.comp0102.nhsxapp.api.background.tasks

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import uclsse.comp0102.nhsxapp.api.R
import uclsse.comp0102.nhsxapp.api.background.noti.NotificationApplier
import uclsse.comp0102.nhsxapp.api.files.JsonFile
import uclsse.comp0102.nhsxapp.api.files.ModelFile
import uclsse.comp0102.nhsxapp.api.files.NhsFileSystem

class NhsDownloadWork(context: Context, workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val jsonFile: JsonFile
    private val modelFile: ModelFile
    private val millisForSingleDay: Long = 1000 * 24 * 60 * 60

    init {
        val file = NhsFileSystem(context)
        val jsonFileDirWithName = context.getString(R.string.JSON_FILE_NAME_WITH_SUB_DIR)
        jsonFile = file.access(JsonFile::class.java, jsonFileDirWithName)
        val modelFileDirWithName = context.getString(R.string.TFL_FILE_NAME_WITH_SUB_DIR)
        modelFile = file.access(ModelFile::class.java, modelFileDirWithName)
    }

    override suspend fun doWork(): Result {
        val lastUploadJsonFileTime = jsonFile.timeForLastUpdate()
        val currentTime = System.currentTimeMillis()
        val isJsonFileUploadOverOneDay =
            (currentTime-lastUploadJsonFileTime) > millisForSingleDay
        if (isJsonFileUploadOverOneDay) return Result.retry()
        val applier = NotificationApplier.getInstance(applicationContext)
        setForeground(applier.apply(this::class.java.name))
        modelFile.update()
        return Result.success()
    }
}