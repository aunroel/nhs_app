package uclsse.comp0102.nhsxapp.api.background.tasks

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import uclsse.comp0102.nhsxapp.api.R
import uclsse.comp0102.nhsxapp.api.background.noti.NotificationApplier
import uclsse.comp0102.nhsxapp.api.files.JsonFile
import uclsse.comp0102.nhsxapp.api.files.ModelFile
import uclsse.comp0102.nhsxapp.api.NhsFileSystem
import uclsse.comp0102.nhsxapp.api.extension.formatSubDir
import uclsse.comp0102.nhsxapp.api.files.RegistrationFile

/** Download work of the model file
 * extends class CoroutineWorker
 */
class NhsDownloadWork(context: Context, workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val jsonFile: JsonFile
    private val modelFile: ModelFile
    private val millisForSingleDay: Long = 1000 * 24 * 60 * 60

    /** Initialization
     * get access to the json and tfl model file with a registerd user ID
     */
    init {
        val file = NhsFileSystem(context)
        val uID = file.access(
            RegistrationFile::class.java,
            context.getString(R.string.REGISTER_FILE_PATH)
        ).uID
        val jsonPath = "${context.getString(R.string.JSON_FILE_SUB_DIR)}/${uID}".formatSubDir()
        jsonFile = file.access(JsonFile::class.java, jsonPath)
        val modelPath = "${context.getString(R.string.TFL_FILE_SUB_DIR)}/${uID}".formatSubDir()
        modelFile = file.access(ModelFile::class.java, modelPath)
    }

    /** Download the model file for a local update, and show notification in foreground
     * return a successful instance
     *  checks the timestamp of last update on json file
     *  if the json file is uploaded over one day then retry
     */
    override suspend fun doWork(): Result {
        val lastUploadJsonFileTime = jsonFile.lastUpdateTime
        val currentTime = System.currentTimeMillis()
        val isJsonFileUploadOverOneDay =
            (currentTime-lastUploadJsonFileTime) > millisForSingleDay
        if (isJsonFileUploadOverOneDay) return Result.retry()
        val applier = NotificationApplier.getInstance(applicationContext)
        setForeground(applier.apply(this::class.java.name))
        modelFile.update()
        return Result.success()
    }
}