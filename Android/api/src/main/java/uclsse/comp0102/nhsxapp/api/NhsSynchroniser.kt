package uclsse.comp0102.nhsxapp.api

import android.content.Context
import androidx.work.WorkerParameters
import uclsse.comp0102.nhsxapp.api.files.AbsOnlineFile
import uclsse.comp0102.nhsxapp.api.synchronise.PeriodicalTaskStarter
import uclsse.comp0102.nhsxapp.api.synchronise.tasks.AbsFilePeriodicalDownloadTask
import uclsse.comp0102.nhsxapp.api.synchronise.tasks.AbsFilePeriodicalUploadTask
import java.time.Duration

/**
 * # 1. Description:
 * The Class are used to create periodically tasks and jobs for uploading and downloading. It
 * encapsulates the Android WorkerManager mechanism, which are able to handle the periodically tasks
 * even though the application did not start on the mobile devices. 
 *
 * # 2. Public Methods:
 * fun startJsonUploadTask(): Unit
 * fun startModelDownloadTask(): Unit
 */
class NhsSynchroniser (appContext: Context){

    private val starter: PeriodicalTaskStarter = PeriodicalTaskStarter(appContext)

    /**
     * Description: It creates a background tasks for upload the json file once a week.
     * */
    fun startJsonUploadTask(){
        starter.setRepeatDuration(Duration.ofDays(7))
        starter.start(JsonFileUploadTask::class.java, "Json file uploading")
    }

    class JsonFileUploadTask(
        context: Context, workerParams: WorkerParameters
    ) : AbsFilePeriodicalUploadTask(context, workerParams){

        override val targetFile: AbsOnlineFile
            get() = NhsFileRepository.getInstance(applicationContext).getJsonFile()

    }

    /**
     * Description: It creates a background tasks for upload the json file once a week.
     * */
    fun startModelDownloadTask(){
        starter.setRepeatDuration(Duration.ofDays(7))
        starter.start(ModelFileDownloadTask::class.java, "Model file downloading")
    }

    class ModelFileDownloadTask(
        context: Context, workerParams: WorkerParameters
    ) : AbsFilePeriodicalDownloadTask(context, workerParams){

        override val targetFile: AbsOnlineFile
            get() = NhsFileRepository.getInstance(applicationContext).getModelFile()
    }
}