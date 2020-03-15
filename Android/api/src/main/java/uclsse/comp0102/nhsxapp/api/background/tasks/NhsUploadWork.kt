package uclsse.comp0102.nhsxapp.api.background.tasks

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import uclsse.comp0102.nhsxapp.api.R
import uclsse.comp0102.nhsxapp.api.background.noti.NotificationApplier
import uclsse.comp0102.nhsxapp.api.files.JsonFile
import uclsse.comp0102.nhsxapp.api.files.ModelFile
import uclsse.comp0102.nhsxapp.api.NhsFileSystem
import uclsse.comp0102.nhsxapp.api.extension.formatSubDir
import uclsse.comp0102.nhsxapp.api.files.RegistrationFile

class NhsUploadWork(context: Context, workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val jsonFile: JsonFile
    private val errorRateFormula =
        { real: Int, predict: Int -> (real - predict) / real.toDouble() }

    init {
        val nhsFileSystem = NhsFileSystem(context)
        val uID = nhsFileSystem.access(
            RegistrationFile::class.java,
            context.getString(R.string.REGISTER_FILE_PATH)
        ).uID
        val jsonPath = "${context.getString(R.string.JSON_FILE_SUB_DIR)}/${uID}".formatSubDir()
        jsonFile = nhsFileSystem.access(JsonFile::class.java, jsonPath)
    }

    override suspend fun doWork(): Result {
        if (jsonFile.lastUploadTime >= jsonFile.lastModifiedTime)
            return Result.retry()
        val applier = NotificationApplier.getInstance(applicationContext)
        setForeground(applier.apply("UPLOADING"))
        jsonFile.upload()
        return Result.success()
    }
}