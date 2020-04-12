package uclsse.comp0102.nhsxapp.api.files.finder

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import uclsse.comp0102.nhsxapp.api.extension.toURL
import uclsse.comp0102.nhsxapp.api.files.AbsOnlineFile
import uclsse.comp0102.nhsxapp.api.files.JsonFile
import uclsse.comp0102.nhsxapp.api.files.ModelFile
import uclsse.comp0102.nhsxapp.api.files.local.database.RecordDatabase
import java.net.URL

class OnlineFileFinderTest {

    companion object{
        const val TEST_HOST_ADDRESS_STR: String =
            "http://10.0.2.2:5000/"
        const val TEST_MODEL_FILE_NAME_AND_DIR:String =
            "/model/e01a469f-abfd-48a6-864f-4f796613b7c4"
        const val TEST_JSON_FILE_NAME_AND_DIR:String =
            "/update/e01a469f-abfd-48a6-864f-4f796613b7c4"
    }

    private lateinit var testFinder: OnlineFileFinder

    private val hostAddress: URL = TEST_HOST_ADDRESS_STR.toURL()
    private val appContext: Context = ApplicationProvider.getApplicationContext()

    @Before
    fun setUp() {
        testFinder = OnlineFileFinder(hostAddress, appContext)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun access() {
        try{
            testFinder.access(
                JsonFile::class.java,
                TEST_JSON_FILE_NAME_AND_DIR
            )
            testFinder.access(
                ModelFile::class.java,
                TEST_MODEL_FILE_NAME_AND_DIR
            )
        } catch (e: Exception){ Assert.fail("UNEXPECTED ERROR") }

        try{
            testFinder.access(
                AbsOnlineFile::class.java,
                TEST_JSON_FILE_NAME_AND_DIR
            )
            Assert.fail("UNEXPECTED PASS")
        } catch (ignore: Exception){}

    }

    @Test
    fun clearAllLocalCache() {
        testFinder.clearAllLocalCache()
        val allData = RecordDatabase.getInstance(appContext).dataAccessor.getAll()
        Truth.assertThat(allData).isEmpty()
    }
}