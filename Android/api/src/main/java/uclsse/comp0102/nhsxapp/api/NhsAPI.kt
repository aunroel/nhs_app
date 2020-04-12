package uclsse.comp0102.nhsxapp.api

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import uclsse.comp0102.nhsxapp.api.extension.exceptionToFlase

/**
 * # 1. Description:
 * The Class specifies APIs for the NHS Android application. It completely follow the design of the
 * MVVM, so that any android application, which use the design pattern (recommended by google), can
 * simple integrate with the class.
 *
 * # 2. Inherit:
 * It is the subclass of the AndroidViewModel, therefore it can be create in any view class of
 * Android, such as Fragment and Activity, by a static method and shared in the whole application
 * context.
 *
 * # 3. Public Methods:
 * fun getTrainingScore(): LiveData<Int?>
 * fun updateTrainingScore(): Unit
 * fun record(newData: Any): LiveData<Boolean?>
 * fun uploadJsonNow(): LiveData<Boolean?>
 * fun updateTfModelNow(): LiveData<Boolean?>
 */
class NhsAPI(application: Application): AndroidViewModel(application){

    private val viewModelJobs: CompletableJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJobs)

    /**
     * ## Description: Override the onCleared method of the android view model, which will be
     * automatically invoked in an Android application to cleared all resource used in the classes.
     * To be specific in the class, it will cancel all coroutine jobs in view model. It means all
     * actions from the framework will be canceled.
     */
    override fun onCleared():Unit {
        super.onCleared()
        viewModelJobs.cancel()
    }

    /**
     * ## Description: get the training score of the local TensorFlow model based on the data in the
     * json file this week. The method will asynchronisedly calculate the new training score based on the
     * data in the json file this week, after that it will automatically update the value of the
     * live data of training score.
     *
     * ## ReturnValue: A live integer
     *
     * PS: The LiveData is a core conception of the MVVM design pattern and android architecture
     * component, which avoid the possible bad user experience and improve the performance of the
     * application. if you want to know more about the LiveData and MVVM, please click url below
     * [LiveData Document](https://developer.android.com/reference/android/arch/lifecycle/LiveData)
     */
    fun getTrainingScoreFromRecords(): LiveData<Int> {
        val trainScoreLiveData = MutableLiveData<Int>()
        AsyncTask.execute {
            val appContext = getApplication<Application>().applicationContext
            val nhsFileRepository = NhsFileRepository.getInstance(appContext)
            val jsonFile = nhsFileRepository.getJsonFile()
            val modelFile = nhsFileRepository.getModelFile()
            val trainingData = jsonFile.readObject(NhsTrainingDataHolder::class.java)
            val inputContainer = floatArrayOf(
                trainingData.realWellBeingScore.toFloat(),
                trainingData.weeklySteps.toFloat(),
                trainingData.weeklyCalls.toFloat()
            )
            val outputContainer = FloatArray(1)
            modelFile.predict(inputContainer, outputContainer)
            uiScope.launch { trainScoreLiveData.value = outputContainer[0].toInt() }
        }
        return trainScoreLiveData
    }

    /**
     * ## Description: The record method will store the data into the json file.
     *
     * ## Argument: The data need to be stored in the json file. It only accepts the instance of
     * NhsTrainingDataHolder and will automatically extract the numbers and strings fields from the
     * input instance.
     *
     * ## ReturnValue: The return value is a live boolean, whose value will change when the data is
     * recorded into database. It returns true when the operation is successfully executed, else it
     * returns false.
     */
    fun record(incomingData: NhsTrainingDataHolder): LiveData<Boolean>{
        val result = MutableLiveData<Boolean>()
        AsyncTask.execute { // Run in another thread.
            val appContext = getApplication<Application>().applicationContext
            val nhsFileRepository = NhsFileRepository.getInstance(appContext)
            val jsonFile = nhsFileRepository.getJsonFile()
            val isJsonFileUploadedThisWeek = jsonFile.lastUploadTime >= jsonFile.lastModifiedTime
            val newData = if (isJsonFileUploadedThisWeek){ // Overwrite the previous data
                incomingData
            } else{ // Merge new values in the object with the previous ones in the json file
                val curData = jsonFile.readObject(NhsTrainingDataHolder::class.java)
                curData.mergeWithNewData(incomingData)
                curData
            }
            val isSuccess =  exceptionToFlase { jsonFile.writeObject(newData) }
            uiScope.launch { result.value = isSuccess }
        }
        return result
    }

    /**
     * ## Description: The method will upload the local json file to the server.
     *
     * ## ReturnValue: The return value is a live boolean, whose value will change after the json
     * file is uploaded. It returns true when the operation is successfully executed, else it
     * returns false.
     */
    fun uploadJsonNow(): LiveData<Boolean>{
        val result = MutableLiveData<Boolean>()
        AsyncTask.execute {
            val appContext = getApplication<Application>().applicationContext
            val nhsFileRepository = NhsFileRepository.getInstance(appContext)
            val isSuccess = nhsFileRepository.getJsonFile().upload()
            uiScope.launch { result.value = isSuccess }
        }
        return result
    }

    /**
     * ## Description: The method will update the local model file from the server.
     *
     * ## ReturnValue: The return value is a live boolean, whose value will change after the model
     * file is updated. It returns true when the operation is successfully executed, else it
     * returns false.
     */
    fun updateTfModelNow(): LiveData<Boolean>{
        val result = MutableLiveData<Boolean>()
        AsyncTask.execute {
            val appContext = getApplication<Application>().applicationContext
            val nhsFileRepository = NhsFileRepository.getInstance(appContext)
            val isSuccess = nhsFileRepository.getModelFile().update()
            uiScope.launch { result.value = isSuccess }
        }
        return result
    }

    fun calculateTrainingDirectlyResultFrom(data: NhsTrainingDataHolder): LiveData<Int>{
        val trainScoreLiveData = MutableLiveData<Int>()
        AsyncTask.execute {
            val appContext = getApplication<Application>().applicationContext
            val nhsFileRepository = NhsFileRepository.getInstance(appContext)
            val modelFile = nhsFileRepository.getModelFile()
            val inputContainer = floatArrayOf(
                data.realWellBeingScore.toFloat(),
                data.weeklySteps.toFloat(),
                data.weeklyCalls.toFloat()
            )
            val outputContainer = FloatArray(1)
            modelFile.predict(inputContainer, outputContainer)
            uiScope.launch { trainScoreLiveData.value = outputContainer[0].toInt() }
        }
        return trainScoreLiveData
    }
}