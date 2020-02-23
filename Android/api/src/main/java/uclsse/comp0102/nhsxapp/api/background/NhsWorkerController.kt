package uclsse.comp0102.nhsxapp.api.background

import android.content.Context
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import uclsse.comp0102.nhsxapp.api.background.workers.iNhsCoroutineWorker
import java.time.Duration

class NhsWorkerController(appContext: Context) {

    private val workerManager = WorkManager.getInstance(appContext)

    fun startWork(worker: iNhsCoroutineWorker, duration: Duration) {
        val request = PeriodicWorkRequest
            .Builder(worker.workType, duration)
            .setConstraints(worker.workConstraints)
            .build()
        workerManager.enqueueUniquePeriodicWork(worker.workName, worker.policy, request)
    }
}