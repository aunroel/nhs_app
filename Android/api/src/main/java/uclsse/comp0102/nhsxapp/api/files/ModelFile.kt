package uclsse.comp0102.nhsxapp.api.files

import android.content.Context
import android.util.Log
import org.tensorflow.lite.Interpreter
import uclsse.comp0102.nhsxapp.api.extension.createNewFileWithDirIfNotExist
import uclsse.comp0102.nhsxapp.api.extension.formatSubDir
import uclsse.comp0102.nhsxapp.api.files.online.HttpClient
import java.io.File
import java.io.IOException
import java.net.URL

/** similar to the JsonFile class, the ModelFile is
 * class also a subclass of the onlineFile. Besides, the
 * ModelFile wrapped the TFlite APIs, so that it can be
 * used to implement machine learning
 */
class ModelFile(
    private val onHost: URL,
    private val subDirWithName: String,
    private val appContext: Context
) : AbsOnlineFile(onHost, subDirWithName, appContext) {

    private var tfLiteInterpreter: Interpreter? = null

    init {
        if(lastUpdateTime == 0L) update()
        loadModel()
    }

    /** update the local tfl model
     */
    override fun updateCore(){
        if (!isOutdated()) throw IOException("online model is not updated")
        val httpClient = HttpClient(onHost)
        val formattedSubDirWithName = subDirWithName.formatSubDir()
        val modelSubDir = formattedSubDirWithName.substringBeforeLast('/')
        val uID = formattedSubDirWithName.substringAfterLast('/')
        val jsonBody = """ {"uid": "$uID"} """
        val newModelBytesArray = httpClient.downloadByPost(jsonBody, modelSubDir)
        writeBytes(newModelBytesArray)
        loadModel()
    }

    override fun uploadCore() {
        TODO("Does not support Federated Machine Learning currently")
    }

    /** prediction using the tfl model
     */
    fun predict(input: FloatArray, output: FloatArray) {
        val inputArray = arrayOf(input)
        val outputArray = arrayOf(output)
        tfLiteInterpreter?.run(inputArray, outputArray)
    }

    private fun isOutdated():Boolean {
        return true
        //TODO: IMPLEMENT THE ONLINE REQUEST
    }

    /** Load the tfl model
     */
    private fun loadModel(){
        val tmpFile = File(appContext.filesDir, subDirWithName)
        tmpFile.createNewFileWithDirIfNotExist()
        tmpFile.writeBytes(readBytes())
        try {
            tfLiteInterpreter = Interpreter(tmpFile)
        }catch (e: IllegalArgumentException){
            e.printStackTrace()
            Log.d("ModelFile.locadModel","the model file is not successfully downloaded.")
        }

    }
}