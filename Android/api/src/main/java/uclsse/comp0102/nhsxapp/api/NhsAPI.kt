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
    private val trainScoreLiveData: MutableLiveData<Int?> = MutableLiveData<Int?>(null)

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
     * json file this week.
     *
     * ## ReturnValue: A live int, whose value will change when the method updateTrainingScore is
     * invoked by the application.
     *
     * PS: The LiveData is a core conception of the MVVM design pattern and android architecture
     * component, which avoid the possible bad user experience and improve the performance of the
     * application. if you want to know more about the LiveData and MVVM, please click url below
     * [LiveData Document](https://developer.android.com/reference/android/arch/lifecycle/LiveData)
     */
    fun getTrainingScore(): LiveData<Int?> {
        return trainScoreLiveData
    }

    /**
     * ## Description: The method will asynchronisedly calculate the new training score based on the
     * data in the json file this week, after that it will automatically update the value of the
     * live data of training score.
     */
    fun updateTrainingScore(): Unit {
        AsyncTask.execute {
            val appContext = getApplication<Application>().applicationContext
            val nhsFileRepository = NhsFileRepository.getInstance(appContext)
            val jsonFile = nhsFileRepository.getJsonFile()
            val modelFile = nhsFileRepository.getModelFile()
            val trainingData = jsonFile.readObject(NhsTrainingDataHolder::class.java)
            val inputContainer = floatArrayOf(
                trainingData.weeklySteps.toFloat(),
                trainingData.weeklyCalls.toFloat(),
                trainingData.weeklyMessages.toFloat()
            )
            val outputContainer = FloatArray(1)
            modelFile.predict(inputContainer, outputContainer)
            uiScope.launch { trainScoreLiveData.value = outputContainer[0].toInt() }
        }
    }

    /**
     * ## Description: The record method will store the data into the json file.
     *
     * ## Argument: The data need to be stored in the json file. It accepts instance of any class,
     * and automatically extract the numbers and strings fields from the input instance.
     *
     * ## ReturnValue: The return value is a live boolean, whose value will change when the data is
     * recorded into database. It returns true when the operation is successfully executed, else it
     * returns false.
     */
    fun record(newData: Any): LiveData<Boolean?>{
        val result = MutableLiveData<Boolean?>(null)
        AsyncTask.execute { // Run in another thread.
            val appContext = getApplication<Application>().applicationContext
            val nhsFileRepository = NhsFileRepository.getInstance(appContext)
            val jsonFile = nhsFileRepository.getJsonFile()
            val isJsonFileUploadedThisWeek = jsonFile.lastUploadTime >= jsonFile.lastModifiedTime
            val isSuccess = if (isJsonFileUploadedThisWeek) // Overwrite the previous data
                exceptionToFlase { jsonFile.writeObject(newData) }
            else // Merge new values in the object with the previous ones in the json file
                exceptionToFlase { jsonFile.mergeObject(newData)}
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
    fun uploadJsonNow(): LiveData<Boolean?>{
        val result = MutableLiveData<Boolean?>(null)
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
    fun updateTfModelNow(): LiveData<Boolean?>{
        val result = MutableLiveData<Boolean?>(null)
        AsyncTask.execute {
            val appContext = getApplication<Application>().applicationContext
            val nhsFileRepository = NhsFileRepository.getInstance(appContext)
            val isSuccess = nhsFileRepository.getModelFile().update()
            uiScope.launch { result.value = isSuccess }
        }
        return result
    }
}