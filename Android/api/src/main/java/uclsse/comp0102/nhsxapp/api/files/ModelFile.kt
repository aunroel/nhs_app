package uclsse.comp0102.nhsxapp.api.files

import android.content.Context
import org.tensorflow.lite.Interpreter
import uclsse.comp0102.nhsxapp.api.extension.createNewFileWithDirIfNotExist
import uclsse.comp0102.nhsxapp.api.extension.formatSubDir
import uclsse.comp0102.nhsxapp.api.files.online.HttpClient
import java.io.File
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
        val tmpFile = File(appContext.filesDir, subDirWithName)
        tmpFile.createNewFileWithDirIfNotExist()
        tmpFile.writeBytes(readBytes())
        tfLiteInterpreter = Interpreter(tmpFile)
    }
}