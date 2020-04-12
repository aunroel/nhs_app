package uclsse.comp0102.nhsxapp.api

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.google.common.truth.Truth
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import uclsse.comp0102.nhsxapp.api.tools.observeOnce

@RunWith(AndroidJUnit4::class)
@LargeTest
class NhsApiInstrumentedTest {

    private lateinit var appContext: Context
    private lateinit var nhsAPI: NhsAPI
    private lateinit var nhsFileRepository: NhsFileRepository

    private val trainingDataOne = NhsTrainingDataHolder().apply{
        this.postCode = "N79X"
        this.supportCode = "Therpists"
        this.weeklyCalls = 30
        this.weeklySteps = 50000
        this.weeklyMessages = 30
        this.realWellBeingScore = 8
        this.predictedWellBeingScore = 7
    }

    private val trainingDataTwo = NhsTrainingDataHolder().apply {
        this.postCode = "X97N"
        this.supportCode = "Surgeon"
        this.weeklyCalls = 10
        this.weeklySteps = 10
        this.weeklyMessages = 10
        this.realWellBeingScore = 8
        this.predictedWellBeingScore = 7
    }

    private val trainingDataOnePlusTwo = NhsTrainingDataHolder().apply {
        this.postCode = "X97N"
        this.supportCode = "Surgeon"
        this.weeklyCalls = 30+10
        this.weeklySteps = 50000+10
        this.weeklyMessages = 30+10
        this.realWellBeingScore = 8
        this.predictedWellBeingScore = 7
    }


    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUpMethods() {
        appContext = ApplicationProvider.getApplicationContext()
        nhsAPI = NhsAPI(appContext as Application)
        nhsFileRepository = NhsFileRepository.getInstance(appContext)
    }

    @After
    fun tearDownMethods() {
        val jsonFile = nhsFileRepository.getJsonFile()
        jsonFile.writeObject(NhsTrainingDataHolder())
    }

    @Test
    fun testInsertNewData(){
        val jsonFile = NhsFileRepository.getInstance(appContext).getJsonFile()
        nhsAPI.record(trainingDataOne.deeplyClone()).observeOnce {oneResult ->
            Truth.assertThat(oneResult).isTrue()
            val dataOne = jsonFile.readObject(NhsTrainingDataHolder::class.java)
            isSameHolderOrNot(dataOne, trainingDataOne)
            nhsAPI.record(trainingDataTwo.deeplyClone()).observeOnce {twoResult->
                Truth.assertThat(twoResult).isTrue()
                val dataMerged = jsonFile.readObject(NhsTrainingDataHolder::class.java)
                isSameHolderOrNot(dataMerged, trainingDataOnePlusTwo)
            }
        }
    }

    @Test
    fun testUpload(){
        val jsonFile = NhsFileRepository.getInstance(appContext).getJsonFile()
        nhsAPI.record(trainingDataOne.deeplyClone()).observeOnce {
            Truth.assertThat(it).isTrue()
            val dataOne = jsonFile.readObject(NhsTrainingDataHolder::class.java)
            nhsAPI.uploadJsonNow().observeOnce { isSuccess ->
                Truth.assertThat(isSuccess).isTrue()
                nhsAPI.record(trainingDataTwo.deeplyClone()).observeOnce {twoResult->
                    Truth.assertThat(twoResult).isTrue()
                    val newData = jsonFile.readObject(NhsTrainingDataHolder::class.java)
                    isSameHolderOrNot(newData, trainingDataTwo)
                }
            }
        }
    }

    @Test
    fun testUpdate(){
        val modelFile = NhsFileRepository.getInstance(appContext).getModelFile()
        val previousUpdateTime = modelFile.lastUpdateTime
        nhsAPI.updateTfModelNow().observeOnce {
            Truth.assertThat(it).isTrue()
            Truth.assertThat(previousUpdateTime).isLessThan(modelFile.lastUpdateTime)
        }
    }

    @Test
    fun testPredict(){
        nhsAPI.record(trainingDataOne).observeOnce {isSuccess->
            Truth.assertThat(isSuccess).isTrue()
            val jsonFile = nhsFileRepository.getJsonFile()
            val dataInJsonFile = jsonFile.readObject(NhsTrainingDataHolder::class.java)
            val modelFile = nhsFileRepository.getModelFile()
            val inputArray = floatArrayOf(
                dataInJsonFile.realWellBeingScore.toFloat(),
                dataInJsonFile.weeklySteps.toFloat(),
                dataInJsonFile.weeklyCalls.toFloat()
            )
            val outputArray = FloatArray(1)
            modelFile.predict(inputArray, outputArray)
            val realOutput = outputArray[0].toInt()
            nhsAPI.getTrainingScoreFromRecords().observeOnce { it ->
                Truth.assertThat(it).isEqualTo(realOutput)
            }
            nhsAPI.calculateTrainingDirectlyResultFrom(dataInJsonFile).observeOnce {
                Truth.assertThat(it).isEqualTo(realOutput)
            }
        }
    }

    private fun isSameHolderOrNot(first: NhsTrainingDataHolder, second: NhsTrainingDataHolder){
        Truth.assertThat(first.errorRate).isEqualTo(second.errorRate)
        Truth.assertThat(first.predictedWellBeingScore).isEqualTo(second.predictedWellBeingScore)
        Truth.assertThat(first.realWellBeingScore).isEqualTo(second.realWellBeingScore)
        Truth.assertThat(first.weeklyCalls).isEqualTo(second.weeklyCalls)
        Truth.assertThat(first.weeklyMessages).isEqualTo(second.weeklyMessages)
        Truth.assertThat(first.weeklySteps).isEqualTo(second.weeklySteps)
        Truth.assertThat(first.postCode).isEqualTo(second.postCode)
        Truth.assertThat(first.supportCode).isEqualTo(second.supportCode)
    }
}