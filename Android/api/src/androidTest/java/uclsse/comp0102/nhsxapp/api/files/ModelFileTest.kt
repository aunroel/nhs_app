package uclsse.comp0102.nhsxapp.api.files

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import uclsse.comp0102.nhsxapp.api.extension.toURL
import java.net.URL

class ModelFileTest {

    companion object{
        const val TEST_HOST_ADDRESS_STR: String =
            "http://10.0.2.2:5000/"
        const val TEST_MODEL_FILE_NAME_AND_DIR:String =
            "/model/e01a469f-abfd-48a6-864f-4f796613b7c4"
    }
    private lateinit var testModelFile: ModelFile

    private val hostAddress: URL = TEST_HOST_ADDRESS_STR.toURL()
    private val appContext: Context = ApplicationProvider.getApplicationContext()


    @Before
    fun setUp() {
        testModelFile = ModelFile(hostAddress,TEST_MODEL_FILE_NAME_AND_DIR ,appContext)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun predict() {
        val inputArrayCorrect = floatArrayOf(1f, 1f, 1f)
        val outputArray = FloatArray(1)
        testModelFile.predict(inputArrayCorrect, outputArray)
        Truth.assertThat(outputArray).isNotEmpty()
        try {
            val inputArrayMiss = floatArrayOf(1f,1f,1f,1f)
            testModelFile.predict(inputArrayMiss, outputArray)
            Assert.fail("Throwable was expected to throw.")
        }catch (ignore: IllegalArgumentException){}
    }
}