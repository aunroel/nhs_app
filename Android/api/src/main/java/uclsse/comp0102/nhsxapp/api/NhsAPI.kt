package uclsse.comp0102.nhsxapp.api

import android.content.Context
import uclsse.comp0102.nhsxapp.api.repository.NhsRepository
import uclsse.comp0102.nhsxapp.api.repository.database.BinaryData
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

    private val fileRepository: NhsRepository =
        NhsRepository(appContext)
    private val workerController: NhsWorkerController =
        NhsWorkerController(appContext)
    private val tfLiteFileSubDirWithName =
        appContext.getString(R.string.TFL_FILE_NAME_WITH_SUB_DIR)
    private val jsonFileSubDirWithName =
        appContext.getString(R.string.JSON_FILE_NAME_WITH_SUB_DIR)


    init {
        val tfLiteFile = BinaryData(tfLiteFileSubDirWithName)
        val jsonDataFile = BinaryData(jsonFileSubDirWithName)
        jsonDataFile.data = "{}".toByteArray()
        fileRepository.add(tfLiteFile, jsonDataFile)
        fileRepository.pull()
    }

    fun getTrainingScore(vararg parameters: Number): Int {
        val trainingDataSet = mutableListOf<Float>()
        parameters.forEach { trainingDataSet.add(it.toFloat()) }
        val outputContainer = FloatArray(1)
        val inputContainer = trainingDataSet.toFloatArray()
        val tfLiteFile = fileRepository.access(MlFile::class.java, tfLiteFileSubDirWithName)
        tfLiteFile.predict(inputContainer, outputContainer)
        return outputContainer[0].toInt()
    }

    fun store(errorRate: Double): Boolean {
        return false
    }

    fun store(data: Any): Boolean {
        try {

            val jsonFile = fileRepository.access(JsonFile::class.java, jsonFileSubDirWithName)
            if (fileRepository.isAllDataClear())
                jsonFile.storeAndOverwrite(data)
            else
                jsonFile.storeAndAccumulate(data)

            return true
        } catch (e: Exception) {
            return false
        }
    }
}