package uclsse.comp0102.nhsxapp.api

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import uclsse.comp0102.nhsxapp.api.background.NhsTaskController
import uclsse.comp0102.nhsxapp.api.background.tasks.NhsDownloadWork
import uclsse.comp0102.nhsxapp.api.background.tasks.NhsUploadWork
import uclsse.comp0102.nhsxapp.api.files.JsonFile
import uclsse.comp0102.nhsxapp.api.files.ModelFile
import uclsse.comp0102.nhsxapp.api.files.NhsFileSystem
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