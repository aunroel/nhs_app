package uclsse.comp0102.nhsxapp.api

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.NetworkType
import uclsse.comp0102.nhsxapp.api.background.NhsTaskController
import uclsse.comp0102.nhsxapp.api.extension.getDay
import uclsse.comp0102.nhsxapp.api.files.JsonFile
import uclsse.comp0102.nhsxapp.api.files.ModelFile
import uclsse.comp0102.nhsxapp.api.files.NhsFileSystem
import java.io.IOException
import java.time.Duration
import java.util.*


class NhsAPI private constructor(appContext: Context) {

    companion object {
        private var core: NhsAPI? = null
        @Synchronized
        fun getInstance(appContext: Context): NhsAPI {
            core = core ?: NhsAPI(appContext)
            return core!!
        }
    }

    private lateinit var jsonFile: JsonFile
    private lateinit var modelFile: ModelFile

    init {
        initFiles(appContext)
        initTasks(appContext)
    }

    private fun initFiles(appContext: Context) {
        val nhsFileSystem = NhsFileSystem(appContext)
        modelFile = nhsFileSystem.access(
            ModelFile::class.java,
            appContext.getString(R.string.TFL_FILE_NAME_WITH_SUB_DIR)
        )
        jsonFile = nhsFileSystem.access(
            JsonFile::class.java,
            appContext.getString(R.string.JSON_FILE_NAME_WITH_SUB_DIR)
        )
    }

    private fun initTasks(appContext: Context) {
        createUploadJsonFileTask(NhsTaskController(appContext))
    }

    private fun createUploadJsonFileTask(controller: NhsTaskController) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicalStarter = controller.PeriodicalTaskStarter()
            .setRepeatDuration(Duration.ofDays(7))
            .setConstraints(constraints)
            .setName("Upload JsonFile")
        periodicalStarter.setTask {
            if (!jsonFile.isModifiedSinceLastUpload)
                throw IOException("Json file did not change")
            val tmpData = jsonFile.to(NhsDataClass::class.java)
            val errorRateFormula = { real: Int, predict: Int -> (real - predict) / real.toDouble() }
            val errorRateValue = errorRateFormula(tmpData.realScore, tmpData.predictedScore)
            jsonFile.storeAndAccumulate(NhsDataClass().apply { errorRate = errorRateValue })
            jsonFile.upload()
            modelFile.upload()
            val timeStamp = Calendar.getInstance().getDay()
            createDownloadModelFileTask(controller, timeStamp)
        }
        periodicalStarter.start()
    }


    private fun createDownloadModelFileTask(controller: NhsTaskController, timeStamp: Int) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val oneTimeStarter = controller.OneTimeTaskStarter()
            .setBackoffPolicy(BackoffPolicy.LINEAR)
            .setBackupDuration(Duration.ofHours(12))
            .setConstraints(constraints)
            .setName("Download ModelFile")
        oneTimeStarter.setTask {
            val currentDay = Calendar.getInstance().getDay()
            if (currentDay == timeStamp)
                throw IOException("download too quick")
            if (modelFile.isModifiedSinceLastUpload)
                throw IOException("cannot overwrite dirty model file ")
            modelFile.update()
        }
    }



    fun getTrainingScore(vararg parameters: Number): Int {
        val trainingDataSet = mutableListOf<Float>()
        parameters.forEach { trainingDataSet.add(it.toFloat()) }
        val outputContainer = FloatArray(1)
        val inputContainer = trainingDataSet.toFloatArray()
        modelFile.predict(inputContainer, outputContainer)
        return outputContainer[0].toInt()
    }

    fun record(data: Any) {
        if (jsonFile.isModifiedSinceLastUpload)
            jsonFile.storeAndAccumulate(data)
        else
            jsonFile.storeAndOverwrite(data)
    }
}