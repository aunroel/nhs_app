package uclsse.comp0102.nhsxapp.api

import android.content.Context
import uclsse.comp0102.nhsxapp.api.periodical.NhsWorkerController
import uclsse.comp0102.nhsxapp.api.repository.NhsRepository
import uclsse.comp0102.nhsxapp.api.repository.files.JsonGlobalFile
import uclsse.comp0102.nhsxapp.api.repository.files.NhsModelGlobalFile
import java.io.File
import java.net.URI


class NhsAPI private constructor(appContext: Context) {

    companion object {
        private var core: NhsAPI? = null

        fun getInstance(): NhsAPI? = core
        fun setContext(value: Context): Boolean {
            if (core != null) return false
            core = NhsAPI(value)
            return true
        }
    }

    private val fileRepository: NhsRepository
    private val workerController: NhsWorkerController

    private val jsonDataFile: JsonGlobalFile
    private val jsonBackupFile: JsonGlobalFile
    private val tfliteFile: NhsModelGlobalFile

    init {
        val onlineURI = URI(appContext.getString(R.string.SERVER_ADDRESS))
        val localURI = File(appContext.filesDir, "repository").toURI()
        NhsRepository.setUri(onlineURI, localURI)
        fileRepository = NhsRepository.instance!!
        workerController = NhsWorkerController(appContext)
        val fileFactory = fileRepository
            .FileBuilder()
            .setFileSubDir(appContext.getString(R.string.SERVER_REPOSITORY_DIR))
        tfliteFile = fileFactory
            .setFileName(appContext.getString(R.string.MODEL_FILE_NAME))
            .build(NhsModelGlobalFile::class.java)
        fileRepository.add(tfliteFile)
        val jsonBackupDefaultContent =
            appContext.getString(R.string.JSON_BACKUP_FILE_DEFAULT_CONTENT)
        fileFactory.setFileContent(jsonBackupDefaultContent.toByteArray(), false)
        jsonDataFile = fileFactory
            .setFileName(appContext.getString(R.string.JSON_DATA_FILE_NAME))
            .build(JsonGlobalFile::class.java)
        fileRepository.add(jsonDataFile)
        jsonBackupFile = fileFactory
            .setFileName(appContext.getString(R.string.JSON_BACKUP_FILE_NAME))
            .build(JsonGlobalFile::class.java)
        fileRepository.add(jsonBackupFile)
    }

    fun getTrainingScore(inputDataType: Class<out TrainingData>): Int {
        val trainingDataSet = mutableListOf<Float>()
        val totalDataSet = jsonBackupFile.toMap()
        inputDataType.declaredFields.forEach {
            val data = totalDataSet[it.name]
            if (data != null) trainingDataSet.add(data.toFloat())
        }
        val outputContainer = FloatArray(1)
        val inputContainer = trainingDataSet.toFloatArray()
        tfliteFile.predict(inputContainer, outputContainer)
        return outputContainer[0].toInt()
    }

    fun store(errorRate: Double): Boolean {
        return false
    }

    fun store(data: StoreData): Boolean = try {
        if (fileRepository.isAllDataClear())
            jsonDataFile.storeDataAndOverwriteDuplication(data)
        else
            jsonDataFile.storeDataAndAccumulateDuplication(data)
        true
    } catch (e: Exception) {
        false
    }
}