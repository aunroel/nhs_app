package uclsse.comp0102.nhsxapp.api

import android.content.Context
import uclsse.comp0102.nhsxapp.api.background.NhsWorkerController
import uclsse.comp0102.nhsxapp.api.background.workers.FileDownloadWorker
import uclsse.comp0102.nhsxapp.api.background.workers.FileUploadWorker
import uclsse.comp0102.nhsxapp.api.repository.NhsFileRepository
import uclsse.comp0102.nhsxapp.api.repository.files.JsonFile
import uclsse.comp0102.nhsxapp.api.repository.files.ModelFile
import java.time.Duration


class NhsAPI private constructor(appContext: Context) {

    companion object {
        private var core: NhsAPI? = null
        @Synchronized
        fun getInstance(appContext: Context): NhsAPI {
            core = core ?: NhsAPI(appContext)
            return core!!
        }
    }

    private val jsonFile: JsonFile
    private val modelFile: ModelFile

    init {
        val workerController = NhsWorkerController(appContext)
        workerController.startWork(FileDownloadWorker(), Duration.ofDays(7))
        workerController.startWork(FileUploadWorker(), Duration.ofDays(7))
        val fileRepository = NhsFileRepository(appContext)
        modelFile = fileRepository.access(
            ModelFile::class.java,
            appContext.getString(R.string.TFL_FILE_NAME_WITH_SUB_DIR)
        )
        jsonFile = fileRepository.access(
            JsonFile::class.java,
            appContext.getString(R.string.JSON_FILE_NAME_WITH_SUB_DIR)
        )
    }

    fun getTrainingScore(vararg parameters: Number): Int {
        val trainingDataSet = mutableListOf<Float>()
        parameters.forEach { trainingDataSet.add(it.toFloat()) }
        val outputContainer = FloatArray(1)
        val inputContainer = trainingDataSet.toFloatArray()
        modelFile.predict(inputContainer, outputContainer)
        return outputContainer[0].toInt()
    }

    fun store(errorRate: Double): Boolean {
        TODO("store the value of $errorRate")
    }

    fun store(data: Any): Boolean {
        return try {
            if (jsonFile.isDirty)
                jsonFile.storeAndAccumulate(data)
            else
                jsonFile.storeAndOverwrite(data)
            true
        } catch (e: Exception) {
            false
        }
    }
}