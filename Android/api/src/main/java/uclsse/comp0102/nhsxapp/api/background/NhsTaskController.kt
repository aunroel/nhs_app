package uclsse.comp0102.nhsxapp.api.background

import android.content.Context
import androidx.work.*
import androidx.work.BackoffPolicy.LINEAR
import uclsse.comp0102.nhsxapp.api.background.tasks.CoroutineOnlineTaskWorker
import uclsse.comp0102.nhsxapp.api.background.tasks.TaskHolder
import uclsse.comp0102.nhsxapp.api.extension.toInputData
import java.time.Duration
import java.time.Duration.ofDays
import androidx.work.ExistingPeriodicWorkPolicy.REPLACE as REPLACE_PERIODIC_POLICY
import androidx.work.ExistingWorkPolicy.REPLACE as REPLACE_POLICY


class NhsTaskController(appContext: Context) {

    private val workManager: WorkManager = WorkManager.getInstance(appContext)

    open inner class OneTimeTaskStarter {
        protected var name: String = "Unknown"
        protected var backupDuration: Duration = ofDays(1)
        protected var backoffPolicy: BackoffPolicy = LINEAR
        protected var constraints: Constraints = Constraints.NONE
        protected lateinit var task: TaskHolder

        fun setName(expected: String): OneTimeTaskStarter {
            name = expected
            return this
        }

        fun setBackoffPolicy(expected: BackoffPolicy): OneTimeTaskStarter {
            backoffPolicy = expected
            return this
        }

        fun setBackupDuration(expected: Duration): OneTimeTaskStarter {
            backupDuration = expected
            return this
        }

        fun setConstraints(expected: Constraints): OneTimeTaskStarter {
            constraints = expected
            return this
        }

        fun setTask(expected: () -> Unit): OneTimeTaskStarter {
            task = TaskHolder().apply { this.innerTask = expected }
            return this
        }

        open fun start() {
            val request = OneTimeWorkRequestBuilder<CoroutineOnlineTaskWorker>()
                .setConstraints(constraints)
                .setInputData(task.toInputData())
                .setBackoffCriteria(backoffPolicy, backupDuration)
                .build()
            workManager.enqueueUniqueWork(name, REPLACE_POLICY, request)
        }
    }

    inner class PeriodicalTaskStarter : OneTimeTaskStarter() {
        private var repeatDuration: Duration = ofDays(7)

        fun setRepeatDuration(expected: Duration): PeriodicalTaskStarter {
            repeatDuration = expected
            return this
        }

        override fun start() {
            val request = PeriodicWorkRequestBuilder<CoroutineWorker>(repeatDuration)
                .setConstraints(constraints)
                .setInputData(task.toInputData())
                .setBackoffCriteria(backoffPolicy, backupDuration)
                .build()
            workManager.enqueueUniquePeriodicWork(name, REPLACE_PERIODIC_POLICY, request)
        }
    }

}