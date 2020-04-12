package uclsse.comp0102.nhsxapp.api.synchronise.tasks

import android.content.Context
import androidx.work.WorkerParameters


abstract class AbsFilePeriodicalUploadTask(
    context: Context, workerParams: WorkerParameters
) : AbsOnlineFilePeriodicalTask(context, workerParams) {

    override suspend fun doWork(): Result {
        return if (targetFile.upload()) Result.success() else Result.retry()
    }
}