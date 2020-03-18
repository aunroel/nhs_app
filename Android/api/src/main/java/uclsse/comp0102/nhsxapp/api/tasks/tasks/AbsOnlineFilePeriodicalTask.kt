package uclsse.comp0102.nhsxapp.api.tasks.tasks

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import uclsse.comp0102.nhsxapp.api.files.AbsOnlineFile

abstract class AbsOnlineFilePeriodicalTask(
    appContext: Context,workerParams: WorkerParameters
): CoroutineWorker(appContext, workerParams) {

    protected abstract val targetFile: AbsOnlineFile

}