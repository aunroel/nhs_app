package uclsse.comp0102.nhsxapp.api

import android.content.Context
import uclsse.comp0102.nhsxapp.api.files.JsonFile
import uclsse.comp0102.nhsxapp.api.files.ModelFile

/**
 * The API for the
 */
class NhsAPI private constructor(appContext: Context) {

    // Static variables to implement Multiton pattern
    // for minimising the memory usage of the package
    companion object {
        private var core: NhsAPI? = null
        @Synchronized
        fun getInstance(appContext: Context): NhsAPI {
            core = core ?: NhsAPI(appContext)
            return core!!
        }
    }

    // Json File and Model file
    private val jsonFile: JsonFile
    private val modelFile: ModelFile

    init {
        val repository = NhsFileRepository(appContext)
        jsonFile = repository.getJsonFile()
        modelFile = repository.getModelFile()
    }

    /**
     * Access the training score from the TensorFlow model
     * The input parameters are model-independent, so that there can be any number of features.
     */
    fun getTrainingScore(vararg parameters: Number): Int {
        val trainingDataSet = mutableListOf<Float>()
        parameters.forEach { trainingDataSet.add(it.toFloat()) }
        val outputContainer = FloatArray(1)
        val inputContainer = trainingDataSet.toFloatArray()
        modelFile.predict(inputContainer, outputContainer)
        return outputContainer[0].toInt()
    }

    /**
     * It can store the data and the method accepts instance of any class,
     * it will automatically extract the numbers and strings fields from the input instance.
     * If the file has already uploaded to the server, then the input data will overwrite current
     * one. Else, they will be merged.
     */
    fun record(newData: Any) {
        val isJsonFileUploadedThisWeek =
            jsonFile.lastUploadTime >= jsonFile.lastModifiedTime
        // if the json file has already been uploaded, then overwrite it.
        // else it will merge the current data with new data.
        if (isJsonFileUploadedThisWeek)
            jsonFile.writeObject(newData)
        else
            jsonFile.mergeObject(newData)
    }

    fun uploadJsonNow(): Boolean{
        return jsonFile.upload()
    }

    fun updateTfModelNow(): Boolean{
        return modelFile.update()
    }
}