package uclsse.comp0102.nhsxapp.api.background.tasks

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import uclsse.comp0102.nhsxapp.api.NhsDataClass
import uclsse.comp0102.nhsxapp.api.R
import uclsse.comp0102.nhsxapp.api.background.noti.NotificationApplier
import uclsse.comp0102.nhsxapp.api.files.JsonFile
import uclsse.comp0102.nhsxapp.api.files.ModelFile
import uclsse.comp0102.nhsxapp.api.files.NhsFileSystem

class NhsUploadWork(context: Context, workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val jsonFile: JsonFile
    private val modelFile: ModelFile
    private val errorRateFormula =
        { real: Int, predict: Int -> (real - predict) / real.toDouble() }

    init {
        val file = NhsFileSystem(context)
        val jsonFileDirWithName = context.getString(R.string.JSON_FILE_NAME_WITH_SUB_DIR)
        jsonFile = file.access(JsonFile::class.java, jsonFileDirWithName)
        val modelFileDirWithName = context.getString(R.string.TFL_FILE_NAME_WITH_SUB_DIR)
        modelFile = file.access(ModelFile::class.java, modelFileDirWithName)
    }

    override suspend fun doWork(): Result {
        if (!jsonFile.isModifiedSinceLastUpload) return Result.retry()
        val applier = NotificationApplier.getInstance(applicationContext)
        setForeground(applier.apply(this::class.java.name))
        val tmpData = jsonFile.to(NhsDataClass::class.java)

        val errorRateValue = errorRateFormula(tmpData.realScore, tmpData.predictedScore)
        jsonFile.storeAndAccumulate(NhsDataClass().apply { errorRate = errorRateValue })
        jsonFile.upload()
        modelFile.upload()
        return Result.success()
    }
}