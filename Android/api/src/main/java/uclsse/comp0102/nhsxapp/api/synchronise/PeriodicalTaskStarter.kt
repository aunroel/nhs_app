package uclsse.comp0102.nhsxapp.api.synchronise

import android.content.Context
import androidx.work.*
import androidx.work.BackoffPolicy.LINEAR
import uclsse.comp0102.nhsxapp.api.synchronise.tasks.AbsOnlineFilePeriodicalTask
import java.time.Duration
import java.time.Duration.ofDays
import androidx.work.ExistingPeriodicWorkPolicy.REPLACE as REPLACE_PERIODIC_POLICY

class PeriodicalTaskStarter(appContext: Context) {

    private val workManager: WorkManager = WorkManager.getInstance(appContext)

    private var backupDuration: Duration = ofDays(1)
    private var backoffPolicy: BackoffPolicy = LINEAR
    private var repeatDuration: Duration = ofDays(7)
    private var constraints: Constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    fun setBackoffPolicy(expected: BackoffPolicy): PeriodicalTaskStarter {
        backoffPolicy = expected
        return this
    }

    fun setBackupDuration(expected: Duration): PeriodicalTaskStarter {
        backupDuration = expected
        return this
    }

    fun setConstraints(expected: Constraints): PeriodicalTaskStarter {
        constraints = expected
        return this
    }

    fun setRepeatDuration(expected: Duration): PeriodicalTaskStarter {
        repeatDuration = expected
        return this
    }

    fun start(typeOfWork: Class<out AbsOnlineFilePeriodicalTask>, name: String) {
        val request = PeriodicWorkRequest
            .Builder(typeOfWork, repeatDuration)
            .setConstraints(constraints)
            .setBackoffCriteria(backoffPolicy, backupDuration)
            .build()
        workManager.enqueueUniquePeriodicWork(name, REPLACE_PERIODIC_POLICY, request)
    }
}