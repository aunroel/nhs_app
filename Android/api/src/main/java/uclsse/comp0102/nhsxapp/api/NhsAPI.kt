package uclsse.comp0102.nhsxapp.api

import android.content.Context
import uclsse.comp0102.nhsxapp.api.background.NhsWorkerController
import uclsse.comp0102.nhsxapp.api.repository.NhsFileRepository
import uclsse.comp0102.nhsxapp.api.repository.files.JsonFile
import uclsse.comp0102.nhsxapp.api.repository.files.MlFile


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
    private val mlFile: MlFile

    init {
        val workerController: NhsWorkerController = NhsWorkerController(appContext)
        val fileRepository: NhsFileRepository = NhsFileRepository(appContext)
        mlFile = fileRepository.access(
            MlFile::class.java,
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
        mlFile.predict(inputContainer, outputContainer)
        return outputContainer[0].toInt()
    }

    fun store(errorRate: Double): Boolean {
        return false
    }

    fun store(data: Any): Boolean {
        return try {
            if (jsonFile.isDirty())
                jsonFile.storeAndAccumulate(data)
            else
                jsonFile.storeAndOverwrite(data)
            true
        } catch (e: Exception) {
            false
        }
    }
}