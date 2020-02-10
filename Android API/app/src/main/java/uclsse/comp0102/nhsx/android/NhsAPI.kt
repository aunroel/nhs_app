package uclsse.comp0102.nhsx.android

import android.content.Context
import androidx.work.*
import uclsse.comp0102.nhsx.android.works.NhsDownloadWork
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
    }

    interface DataClass{ }

    fun getTrainingScore(): Int{ return 0 }

    fun store(errorRate: Double): Boolean{ return false }

    fun store(data: NhsAPI.DataClass): Boolean{ return false }


}