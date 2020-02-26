package uclsse.comp0102.nhsxapp.api.background.tasks

import android.accounts.NetworkErrorException
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker.Result.*
import androidx.work.WorkerParameters
import uclsse.comp0102.nhsxapp.api.background.noti.NotificationApplier
import uclsse.comp0102.nhsxapp.api.extension.getTask
import java.io.IOException

class CoroutineOnlineTaskWorker(
    context: Context, workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val task: () -> Unit = workerParams.getTask()?.innerTask!!

    override suspend fun doWork(): Result {
        return try {
            val applier = NotificationApplier.getInstance(applicationContext)
            setForeground(applier.apply(this::class.java.name))
            run(task)
            success()
        } catch (e: IOException) {
            retry()
        } catch (e: NetworkErrorException) {
            retry()
        } catch (e: Exception) {
            failure()
        }
    }
}