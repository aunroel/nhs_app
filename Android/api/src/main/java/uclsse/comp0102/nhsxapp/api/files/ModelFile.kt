package uclsse.comp0102.nhsxapp.api.files

import android.content.Context
import org.tensorflow.lite.Interpreter
import uclsse.comp0102.nhsxapp.api.extension.createNewFileWithDirIfNotExist
import uclsse.comp0102.nhsxapp.api.files.online.HttpClient
import java.io.File
import java.lang.Exception
import java.net.URL

/** similar to the JsonFile class, the ModelFile is
 * class also a subclass of the onlineFile. Besides, the
 * ModelFile wrapped the TFlite APIs, so that it can be
 * used to implement machine learning
 */
class ModelFile(onHost: URL, subDirWithName: String, appContext: Context) :
    AbsOnlineFile(onHost, subDirWithName, appContext) {

    private val appContext: Context = appContext

    private val hostAddress: URL = onHost
    private val dirWithName: String = subDirWithName

    private var tfLiteInterpreter: Interpreter? = null



    init {
        if(lastUpdateTime == 0L)
            update()
        loadModel()
    }

    /** update the local tfl model
     */
    override fun updateCore(){
        val httpClient = HttpClient(hostAddress)
        val newModelBytesArray = httpClient.get(dirWithName)
        writeBytes(newModelBytesArray)
        loadModel()
    }

    override fun uploadCore() {
        TODO("Not yet implemented")
    }

    /** prediction using the tfl model
     */
    fun predict(input: FloatArray, output: FloatArray) {
        val inputArray = arrayOf(input)
        val outputArray = arrayOf(output)
        tfLiteInterpreter?.run(inputArray, outputArray)
    }

    /** Load the tfl model
     */
    private fun loadModel(){
        val tmpFile = File(appContext.filesDir, dirWithName)
        tmpFile.createNewFileWithDirIfNotExist()
        tmpFile.writeBytes(readBytes())
        tfLiteInterpreter = Interpreter(tmpFile)
    }
}