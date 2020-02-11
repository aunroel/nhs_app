package uclsse.comp0102.nhsx.android

import android.content.Context
import androidx.work.*
import uclsse.comp0102.nhsx.android.works.NhsDownloadWork
import uclsse.comp0102.nhsx.android.works.NhsUploadWork
import java.util.concurrent.TimeUnit.DAYS


class NhsAPI private constructor(context: Context){

    companion object {
        private val core = mutableMapOf<Context, NhsAPI>()
        fun getInstance(byContext: Context): NhsAPI{
            if(!core.containsKey(byContext))
                core[byContext] = NhsAPI(byContext)
            return core[byContext]!!
        }
    }

    init {
        val workerManager = WorkManager.getInstance(context)

        val uploadRequest =  PeriodicWorkRequestBuilder<NhsUploadWork>(7, DAYS)
            .setConstraints(NhsUploadWork.WORK_CONSTRAINTS)
            .build()
        val downloadRequest =  PeriodicWorkRequestBuilder<NhsDownloadWork>(7, DAYS)
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

    interface DataClass{ }

    fun getTrainingScore(): Int{ return 0 }

    fun store(errorRate: Double): Boolean{ return false }

    fun store(data: NhsAPI.DataClass): Boolean{ return false }


}