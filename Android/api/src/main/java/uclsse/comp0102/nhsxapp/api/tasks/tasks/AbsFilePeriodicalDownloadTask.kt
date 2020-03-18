package uclsse.comp0102.nhsxapp.api.tasks.tasks

import android.content.Context
import androidx.work.WorkerParameters

abstract class AbsFilePeriodicalDownloadTask(
    context: Context, workerParams: WorkerParameters
) : AbsOnlineFilePeriodicalTask(context, workerParams) {


    override suspend fun doWork(): Result {
        return if (targetFile!!.update()) Result.success() else Result.retry()
    }
}