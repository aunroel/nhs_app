package uclsse.comp0102.nhsxapp.api

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.collect.Range
import com.google.common.truth.Truth
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import uclsse.comp0102.nhsxapp.api.extension.formatSubDir
import uclsse.comp0102.nhsxapp.api.files.JsonFile
import uclsse.comp0102.nhsxapp.api.files.ModelFile
import uclsse.comp0102.nhsxapp.api.files.RegistrationFile

@RunWith(AndroidJUnit4::class)
class ServerIntegrationTest {
    private var modelFile: ModelFile
    private var jsonFile: JsonFile


    init {
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        val nhsFileSystem = NhsFileSystem(appContext)
        val registrationFile = nhsFileSystem.access(
            RegistrationFile::class.java,
            appContext.getString(R.string.REGISTER_FILE_PATH)
        )
        val tflFileSubDir = appContext.getString(R.string.TFL_FILE_SUB_DIR)
        modelFile = nhsFileSystem.access(
            ModelFile::class.java,
            "${tflFileSubDir}/${registrationFile.uID}".formatSubDir()
        )
        val jsonFileSubDir = appContext.getString(R.string.JSON_FILE_SUB_DIR)
        jsonFile = nhsFileSystem.access(
            JsonFile::class.java,
            "${jsonFileSubDir}/${registrationFile.uID}".formatSubDir()

        )
    }

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun testUploadJson(){
        val data = NhsTrainingDataHolder().apply {
            supportCode = "NHS_TEST"
            postCode = "NHS"
            predictedWellBeingScore = 5
            realWellBeingScore = 4
            weeklyCalls = 1000
            weeklySteps = 1000
            weeklyMessages = 1000
        }

        jsonFile.writeObject(data)
        jsonFile.upload()
    }

    @Test
    fun testPredict() {
        val model = modelFile
        val input = floatArrayOf(100f, 100f, 100f)
        val outputContainer = FloatArray(1)
        model.predict(input, outputContainer)
        val actual = outputContainer[0]
        val expectedRange = Range.closed(-3 - 0.2f, -3 + 0.2f)
        Truth.assertThat(actual).isIn(expectedRange)
    }
}