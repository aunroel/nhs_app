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
import uclsse.comp0102.nhsxapp.api.repository.NhsFileRepository
import uclsse.comp0102.nhsxapp.api.repository.files.ModelFile

@RunWith(AndroidJUnit4::class)
class ModelFileTest {

    private val nhsRepository: NhsFileRepository
    private val modelFileDirAndName: String


    private var modelFile: ModelFile? = null


    init {
        val context = ApplicationProvider.getApplicationContext<Context>()
        nhsRepository = NhsFileRepository(context)
        modelFileDirAndName = context.getString(R.string.TEST_MODEL_FILE_PATH)
    }

    @Before
    fun setUp() {
        modelFile = nhsRepository.access(
            ModelFile::class.java,
            modelFileDirAndName
        )
    }

    @After
    fun tearDown() {
        nhsRepository.clearAllLocalCache()
        modelFile = null
    }

    @Test
    fun testPredict() {
        val model = modelFile!!
        val input = floatArrayOf(-1f, -1f)
        val outputContainer = FloatArray(1)
        model.predict(input, outputContainer)
        val actual = outputContainer[0]
        val expectedRange = Range.closed(-3 - 0.2f, -3 + 0.2f)
        Truth.assertThat(actual).isIn(expectedRange)
    }

}