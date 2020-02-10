package uclsse.comp0102.nhsx.android

import androidx.work.WorkManager
import androidx.work.ExistingPeriodicWorkPolicy
import uclsse.comp0102.nhsx.android.works.NhsDownloadWork

class WorkerRequester {

    fun request(){
        val workerManager = WorkManager.getInstance(context)
        workerManager.enqueueUniquePeriodicWork(
            NhsDownloadWork.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatRequest
        )
    }

}