package uclsse.comp0102.nhsxapp.api

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import uclsse.comp0102.nhsxapp.api.tools.observeOnce

@RunWith(AndroidJUnit4::class)
class NhsApiTest {

    private lateinit var appContext: Context
    private lateinit var nhsAPI: NhsAPI
    private lateinit var nhsFileRepository: NhsFileRepository

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        appContext = ApplicationProvider.getApplicationContext()
        nhsAPI = NhsAPI(appContext as Application)
        nhsFileRepository = NhsFileRepository.getInstance(appContext)
    }

    @After
    fun tearDown() {
        val jsonFile = nhsFileRepository.getJsonFile()
        jsonFile.writeObject(NhsTrainingDataHolder())
    }

    private fun generateTrainingData(): NhsTrainingDataHolder{
        val getStrResource:(Int)->String = {id -> appContext.resources.getString(id)}
        val getIntResource:(Int)->Int = {id ->  appContext.resources.getInteger(id)}
        return NhsTrainingDataHolder().apply {
            this.postCode = getStrResource(R.string.TEST_VM_POSTCODE)
            this.supportCode = getStrResource(R.string.TEST_VM_SUPPORT_CODE)
            this.weeklyCalls = getIntResource(R.integer.TEST_VM_CALLS_NUM)
            this.weeklySteps = getIntResource(R.integer.TEST_VM_STEPS_NUM)
            this.weeklyMessages = getIntResource(R.integer.TEST_VM_MSGS_NUM)
            this.realWellBeingScore = getIntResource(R.integer.TEST_VM_WELLBEING_REAL)
            this.predictedWellBeingScore = getIntResource(R.integer.TEST_VM_WELLBEING_REAL)
        }
    }

    @Test
    fun testInsertNewData() = nhsAPI.record(generateTrainingData()).observeOnce {
        Truth.assertThat(it).isTrue()
        val jsonFile = NhsFileRepository.getInstance(appContext).getJsonFile()
        val dataInJsonFile = jsonFile.readObject(NhsTrainingDataHolder::class.java)
        Truth.assertThat(dataInJsonFile).isEqualTo(generateTrainingData())
    }

    @Test
    fun testUpload(){

    }

    @Test
    fun testUpdate(){

    }

    @Test
    fun testPredict() = nhsAPI.record(generateTrainingData()).observeOnce {
        Truth.assertThat(it).isTrue()
        nhsAPI.getTrainingScoreFromRecords().observeOnce { apiOutput ->
            val repository = NhsFileRepository.getInstance(appContext)
            val jsonFile = repository.getJsonFile()
            val dataInJsonFile = jsonFile.readObject(NhsTrainingDataHolder::class.java)
            val modelFile = repository.getModelFile()
            val inputArray = floatArrayOf(
                dataInJsonFile.weeklyMessages.toFloat(),
                dataInJsonFile.weeklyCalls.toFloat(),
                dataInJsonFile.weeklySteps.toFloat()
            )
            val outputArray = FloatArray(1)
            modelFile.predict(inputArray, outputArray)
            val realOutput = outputArray[0]
            Truth.assertThat(apiOutput).isEqualTo(realOutput)
        }
    }

}