package uclsse.comp0102.nhsxapp.api

import android.content.Context
import androidx.work.WorkerParameters
import uclsse.comp0102.nhsxapp.api.files.AbsOnlineFile
import uclsse.comp0102.nhsxapp.api.tasks.PeriodicalTaskStarter
import uclsse.comp0102.nhsxapp.api.tasks.tasks.AbsFilePeriodicalDownloadTask
import uclsse.comp0102.nhsxapp.api.tasks.tasks.AbsFilePeriodicalUploadTask

class NhsSynchroniser (appContext: Context){

    private val starter: PeriodicalTaskStarter = PeriodicalTaskStarter(appContext)

    fun startJsonUploadTask(){
        starter.start(JsonFileUploadTask::class.java, "Json file uploading")
    }

    fun startModelDownloadTask(){
        starter.start(ModelFileDownloadTask::class.java, "Model file downloading")
    }

    class JsonFileUploadTask(
        context: Context, workerParams: WorkerParameters
    ) : AbsFilePeriodicalUploadTask(context, workerParams){

        override val targetFile: AbsOnlineFile
            get() = NhsFileRepository(applicationContext).getJsonFile()

    }

    class ModelFileDownloadTask(
        context: Context, workerParams: WorkerParameters
    ) : AbsFilePeriodicalDownloadTask(context, workerParams){

        override val targetFile: AbsOnlineFile
            get() = NhsFileRepository(applicationContext).getModelFile()
    }
}