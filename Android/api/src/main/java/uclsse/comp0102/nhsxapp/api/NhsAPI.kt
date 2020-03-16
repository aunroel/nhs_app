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

    // Static variables to implement Multiton pattern
    // for minimising the memory usage of the package
    companion object {
        private var core: NhsAPI? = null
        @Synchronized
        fun getInstance(appContext: Context): NhsAPI {
            core = core ?: NhsAPI(appContext)
            return core!!
        }
    }

    // Json File and Model file
    private lateinit var jsonFile: JsonFile
    private lateinit var modelFile: ModelFile

    init {
        val nhsFileSystem = NhsFileSystem(appContext)
        // Access the uID of the application by the RegistrationFile
        val registrationFile = nhsFileSystem.access(
            RegistrationFile::class.java,
            appContext.getString(R.string.REGISTER_FILE_PATH)
        )
        // Access the json file and model file
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
        //initTask(appContext)
    }

    // creating of the periodically Tasks for downloading and uploading files
    fun initTasks(appContext: Context) {
        // Constraint: Must have network connection
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        // Create a task for periodically Upload Json File
        val nhsTaskController = NhsTaskController(appContext)
        nhsTaskController.PeriodicalTaskStarter()
            .setRepeatDuration(Duration.ofDays(7))
            .setConstraints(constraints)
            .setBackupDuration(Duration.ofHours(3))
            .setWorkType(NhsUploadWork::class.java)
        // Create a task for periodically download the model file
        nhsTaskController.PeriodicalTaskStarter()
            .setRepeatDuration(Duration.ofDays(7))
            .setConstraints(constraints)
            .setBackupDuration(Duration.ofHours(12))
            .setWorkType(NhsDownloadWork::class.java)
    }

    // Access the training score from the TensorFlow model
    // The input parameters are model-independent, so that there
    // can be any number of features.
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

    // it can store the data and the method accepts instance of any class,
    // it will automatically extract the numbers and strings fields from
    // the input instance.
    fun record(newData: Any) {
        // if the json file has already been uploaded, then overwrite it.
        if (jsonFile.lastUploadTime >= jsonFile.lastModifiedTime)
            jsonFile.writeObject(newData)
        // else it will merge the current data and new data.
        val dataType = newData::class.java
        val currentData = jsonFile.readObject(dataType)
        dataType.declaredFields.forEach {
            val accessibleBak = it.isAccessible
            it.isAccessible = true
            when {
                it.isNumberType -> {
                    val newValue = (it.get(newData)?:0) as Number
                    val curValue = (it.get(currentData) ?: 0) as Number
                    val finalValue = curValue plus newValue
                    it.set(currentData, finalValue)
                }
                it.isStringType -> {
                    val newValue = (it.get(newData)?:"") as String
                    val curValue = (it.get(currentData) ?: "") as String
                    val finalValue = if(newValue == "") curValue else newValue
                    it.set(currentData, finalValue)

                }
                else -> {
                    val finalValue = it.get(newData) ?: it.get(currentData)
                    it.set(currentData, finalValue)
                }
            }
            it.isAccessible = accessibleBak
        }
        jsonFile.writeObject(currentData)
    }

    fun uploadJsonNow(){
        jsonFile.upload()
    }

    fun updateTfModelNow(){
        modelFile.update()
    }

    enum class ModelType{
        Local, Global
    }
}