package uclsse.comp0102.nhsxapp.api

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import uclsse.comp0102.nhsxapp.api.works.NhsDownloadWork
import uclsse.comp0102.nhsxapp.api.works.NhsUploadWork
import java.time.Duration


class NhsAPI private constructor(context: Context){

    companion object {
        private val core = mutableMapOf<Context, NhsAPI>()

        fun getInstance(byContext: Context): NhsAPI {
            if(!core.containsKey(byContext))
                core[byContext] =
                        NhsAPI(byContext)
            return core[byContext]!!
        }
    }

    init {
        val workerManager = WorkManager.getInstance(context)

        val requestBuilder = PeriodicWorkRequest.Builder(
                NhsUploadWork::class.java,
                Duration.ofDays(7)
        )

        val uploadRequest = requestBuilder
            .setConstraints(NhsUploadWork.WORK_CONSTRAINTS)
            .build()

        val downloadRequest = requestBuilder
            .setConstraints(NhsDownloadWork.WORK_CONSTRAINTS)
            .build()

        workerManager.enqueueUniquePeriodicWork(
            NhsUploadWork.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            uploadRequest
        )

        workerManager.enqueueUniquePeriodicWork(
            NhsDownloadWork.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            downloadRequest
        )
    }

    interface DataClass

    fun getTrainingScore(): Int{ return 0 }

    fun store(errorRate: Double): Boolean{ return false }

    fun store(data: DataClass): Boolean {
        return false
    }


}