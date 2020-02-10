package uclsse.comp0102.nhsxapp.nhsapp

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import uclsse.comp0102.nhsx.android.works.NhsDownloadWork
import androidx.work.ListenableWorker.Result
import androidx.work.testing.TestListenableWorkerBuilder
import kotlinx.coroutines.runBlocking

@RunWith(AndroidJUnit4::class)
class DownloadWorkTest {
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun testDoWorkOnTimes() {
        val worker = TestListenableWorkerBuilder<NhsDownloadWork>(context).build()
        runBlocking {
            val result = worker.doWork()
            assert(result == (Result.success()))
        }
    }


}