package uclsse.comp0102.nhsxapp.api

import android.content.Context
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker.Result
import androidx.work.testing.TestListenableWorkerBuilder
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import uclsse.comp0102.nhsxapp.api.background.tasks.CoroutineOnlineTaskWorker
import uclsse.comp0102.nhsxapp.api.background.tasks.TaskHolder
import uclsse.comp0102.nhsxapp.api.extension.toInputData

@RunWith(AndroidJUnit4::class)

class BackgroundTest {
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun testSleepWorker() {
        val holder = TaskHolder().apply {
            innerTask = { Log.d("TEST", "this is in worker") }
        }
        val worker =
            TestListenableWorkerBuilder<CoroutineOnlineTaskWorker>(context = context)
                .setInputData(holder.toInputData())
                .build() as CoroutineOnlineTaskWorker

        runBlocking {
            val result = worker.doWork()
            assertThat(worker.doWork()).isEqualTo(Result.success())
        }
    }
}
