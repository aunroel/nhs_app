package uclsse.comp0102.nhsxapp.api

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.collect.Range
import com.google.common.truth.Truth
import org.junit.After
import org.junit.Before
import org.junit.Test
import uclsse.comp0102.nhsxapp.api.repository.files.NhsModelGlobalFile
import java.net.URI

class PredictTest {

    private var file: NhsModelGlobalFile? = null
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val onlineUrl = URI.create("http://10.0.2.2:5000/")
    private val localUri = context.filesDir.toURI()
    private val name = "model.tflite"


    @Before
    fun setUp() {
        file = NhsModelGlobalFile(onlineUrl, localUri, name)
    }

    @After
    fun tearDown() {
        if (file != null && file!!.exists()) file?.delete()
    }

    @Test
    fun testPredict() {
        file?.downloadOnlineVersion("static")
        val input = floatArrayOf(-1f, -1f)
        val expectedOutputRange = Range.closed(-3 - 0.2f, -3 + 0.2f)
        val outputContainer = FloatArray(1)
        file?.predict(input, outputContainer)
        val realOutput = outputContainer[0]
        Truth.assertThat(realOutput).isIn(expectedOutputRange)
    }

}