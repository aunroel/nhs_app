package uclsse.comp0102.nhsxapp.api.background

import android.content.Context
import androidx.work.*
import androidx.work.BackoffPolicy.LINEAR
import java.time.Duration
import java.time.Duration.ofDays
import androidx.work.ExistingPeriodicWorkPolicy.REPLACE as REPLACE_PERIODIC_POLICY
import androidx.work.ExistingWorkPolicy.REPLACE as REPLACE_POLICY

/** Control a whole prediction task
 */
class NhsTaskController(appContext: Context) {

    private val workManager: WorkManager = WorkManager.getInstance(appContext)

    /** Initialization and run of a one-time task
     */
    open inner class OneTimeTaskStarter {
        protected var name: String = "Unknown"
        protected var backupDuration: Duration = ofDays(1)
        protected var backoffPolicy: BackoffPolicy = LINEAR
        protected var constraints: Constraints = Constraints.NONE
        protected lateinit var typeOfWork: Class<out ListenableWorker>

        /** set the name
         * return an instance of OneTimeTaskStarter
         */
        fun setName(expected: String): OneTimeTaskStarter {
            name = expected
            return this
        }

        /** set the back-off policy
         * return an instance of OneTimeTaskStarter
         */
        fun setBackoffPolicy(expected: BackoffPolicy): OneTimeTaskStarter {
            backoffPolicy = expected
            return this
        }

        /** set the backup duration
         * return an instance of OneTimeTaskStarter
         */
        fun setBackupDuration(expected: Duration): OneTimeTaskStarter {
            backupDuration = expected
            return this
        }

        /** set the constrains
         * return an instance of OneTimeTaskStarter
         */
        fun setConstraints(expected: Constraints): OneTimeTaskStarter {
            constraints = expected
            return this
        }

        /** set the type of work
         * return an instance of OneTimeTaskStarter
         */
        fun setWorkType(expected: Class<out ListenableWorker>):OneTimeTaskStarter{
            typeOfWork = expected
            return this
        }

        /** Run a one-time task with initialized parameters(name, back-off policy, backup duration, constrains and type of work)
         */
        open fun start() {
            val request = OneTimeWorkRequest
                .Builder(typeOfWork)
                .setConstraints(constraints)
                .setBackoffCriteria(backoffPolicy, backupDuration)
                .build()
            workManager.enqueueUniqueWork(name, REPLACE_POLICY, request)
        }
    }

    /** Initialization and run of a periodical task
     * extends the OneTimeTaskStarter class
     */
    inner class PeriodicalTaskStarter : OneTimeTaskStarter() {
        private var repeatDuration: Duration = ofDays(7)

        /** set the repeat duration of the task
         * return an instance of PeriodicalTaskStarter
         */
        fun setRepeatDuration(expected: Duration): PeriodicalTaskStarter {
            repeatDuration = expected
            return this
        }

        /** Run a periodical task with initialized parameters
         */
        override fun start() {
            val request = PeriodicWorkRequest
                .Builder(typeOfWork, repeatDuration)
                .setConstraints(constraints)
                .setBackoffCriteria(backoffPolicy, backupDuration)
                .build()
            workManager.enqueueUniquePeriodicWork(name, REPLACE_PERIODIC_POLICY, request)
        }
    }

}