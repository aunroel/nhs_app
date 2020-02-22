package uclsse.comp0102.nhsxapp.api.works

import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy


interface iNhsCoroutineWorker {
    val workName: String
    val workType: Class<out CoroutineWorker>
    val workConstraints: Constraints
    val policy: ExistingPeriodicWorkPolicy

}