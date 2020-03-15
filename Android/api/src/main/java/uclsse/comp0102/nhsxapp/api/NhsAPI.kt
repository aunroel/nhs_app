package uclsse.comp0102.nhsxapp.api

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import uclsse.comp0102.nhsxapp.api.background.NhsTaskController
import uclsse.comp0102.nhsxapp.api.background.tasks.NhsDownloadWork
import uclsse.comp0102.nhsxapp.api.background.tasks.NhsUploadWork
import uclsse.comp0102.nhsxapp.api.extension.formatSubDir
import uclsse.comp0102.nhsxapp.api.extension.isNumberType
import uclsse.comp0102.nhsxapp.api.extension.isStringType
import uclsse.comp0102.nhsxapp.api.extension.plus
import uclsse.comp0102.nhsxapp.api.files.JsonFile
import uclsse.comp0102.nhsxapp.api.files.ModelFile
import uclsse.comp0102.nhsxapp.api.files.RegistrationFile
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

    private val uID = "e01a469f-abfd-48a6-864f-4f796613b7c4"
    private lateinit var jsonFile: JsonFile
    private lateinit var modelFile: ModelFile

    fun initFiles(appContext: Context) {
        val nhsFileSystem = NhsFileSystem(appContext)
        val registrationFile = nhsFileSystem.access(
            RegistrationFile::class.java,
            appContext.getString(R.string.REGISTER_FILE_PATH)
        )
        val tflFileSubDir = appContext.getString(R.string.TFL_FILE_SUB_DIR)
        modelFile = nhsFileSystem.access(
            ModelFile::class.java,
            "${tflFileSubDir}/${registrationFile.uID}".formatSubDir()
        )
        val jsonFileSubDir = appContext.getString(R.string.JSON_FILE_SUB_DIR)
        jsonFile = nhsFileSystem.access(
            JsonFile::class.java,
            "${jsonFileSubDir}/${registrationFile.uID}".formatSubDir()
        )
    }

    fun initTasks(appContext: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val nhsTaskController = NhsTaskController(appContext)
        nhsTaskController.PeriodicalTaskStarter()
            .setRepeatDuration(Duration.ofDays(7))
            .setConstraints(constraints)
            .setBackupDuration(Duration.ofHours(3))
            .setWorkType(NhsUploadWork::class.java)

        nhsTaskController.PeriodicalTaskStarter()
            .setRepeatDuration(Duration.ofDays(7))
            .setConstraints(constraints)
            .setBackupDuration(Duration.ofHours(12))
            .setWorkType(NhsDownloadWork::class.java)
    }

    fun getTrainingScore(
        vararg parameters: Number,
        fromModelType:ModelType = ModelType.Local
    ): Int {
        val trainingDataSet = mutableListOf<Float>()
        parameters.forEach { trainingDataSet.add(it.toFloat()) }
        val outputContainer = FloatArray(1)
        val inputContainer = trainingDataSet.toFloatArray()
        modelFile.predict(inputContainer, outputContainer)
        return outputContainer[0].toInt()
    }

    fun record(data: Any) {
        if (jsonFile.lastUploadTime >= jsonFile.lastModifiedTime)
            jsonFile.writeObject(data)
        val dataType = data::class.java
        val currentData = jsonFile.readObject(dataType)
        dataType.declaredFields.forEach {
            when {
                it.isNumberType -> {
                    val newValue = (it.get(data)?:0) as Number
                    val curValue = (it.get(currentData) ?: 0) as Number
                    val finalValue = curValue plus newValue
                    it.set(currentData, finalValue)
                }
                it.isStringType -> {
                    val newValue = (it.get(data)?:"") as String
                    val curValue = (it.get(currentData) ?: "") as String
                    val finalValue = if(newValue == "") curValue else newValue
                    it.set(currentData, finalValue)

                }
                else -> {
                    val finalValue = it.get(data) ?: it.get(currentData)
                    it.set(currentData, finalValue)
                }
            }
        }
        jsonFile.writeObject(currentData)
    }

    enum class ModelType{
        Local, Global
    }
}