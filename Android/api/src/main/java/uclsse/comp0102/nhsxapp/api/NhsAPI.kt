package uclsse.comp0102.nhsxapp.api

import android.content.Context
import uclsse.comp0102.nhsxapp.api.extension.*
import uclsse.comp0102.nhsxapp.api.files.JsonFile
import uclsse.comp0102.nhsxapp.api.files.ModelFile

/** Open API for prediction
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

    /** Access the training score from the TensorFlow model
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

    /** It can store the data and the method accepts instance of any class,
     * it will automatically extract the numbers and strings fields from the input instance.
     */
    fun record(newData: Any) {
        // if the json file has already been uploaded, then overwrite it.
        if (jsonFile.lastUploadTime >= jsonFile.lastModifiedTime)
            jsonFile.writeObject(newData)
        // else it will merge the current data and new data.
        val currentData = jsonFile.readObject(newData::class.java)
        newData::class.java.forEachField {
            if (it.isNumberType){
                val newValue = (it.get(newData)?:0) as Number
                val curValue = (it.get(currentData) ?: 0) as Number
                val finalValue = curValue plus newValue
                it.set(currentData, finalValue)
            } else if(it.isStringType) {
                val newValue = (it.get(newData)?:"") as String
                val curValue = (it.get(currentData) ?: "") as String
                val finalValue = if(newValue == "") curValue else newValue
                it.set(currentData, finalValue)
            } else{
                val finalValue = it.get(newData) ?: it.get(currentData)
                it.set(currentData, finalValue)
            }
        }
        jsonFile.writeObject(currentData)
    }

    fun uploadJsonNow(): Boolean{
        return jsonFile.upload()
    }

    fun updateTfModelNow(): Boolean{
        return modelFile.update()
    }
}