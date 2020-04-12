package uclsse.comp0102.nhsxapp.api

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)

class NhsSynchroniserInstrumentedTest {
    private lateinit var appContext: Context
    private lateinit var nhsFileRepository: NhsFileRepository
    private lateinit var nhsSynchroniser: NhsSynchroniser

    @Before
    fun setUp() {
        appContext = ApplicationProvider.getApplicationContext()
        nhsFileRepository = NhsFileRepository.getInstance(appContext)
        nhsSynchroniser = NhsSynchroniser(appContext)
    }

    @Test
    fun testUploadSynchroniser() {
        /*
         * Currently there is only very basic framework for testing the workmanager mechanism,
         * and the test code is almost same with that in the NhsAPI.
         */
    }

    @Test
    fun testUpdataSynchroniser() {
        /*
         * Currently there is only very basic framework for testing the workmanager mechanism,
         * and the test code is almost same with that in the NhsAPI.
         */
    }

}
