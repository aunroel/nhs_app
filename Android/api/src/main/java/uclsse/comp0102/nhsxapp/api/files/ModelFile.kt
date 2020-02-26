package uclsse.comp0102.nhsxapp.api.files

import android.content.Context
import org.tensorflow.lite.Interpreter
import uclsse.comp0102.nhsxapp.api.extension.createNewFileWithDirIfNotExist
import uclsse.comp0102.nhsxapp.api.files.core.OnlineFile
import java.io.File
import java.net.URL


class ModelFile(onHost: URL, subDirWithName: String, appContext: Context) :
    OnlineFile(onHost, subDirWithName, appContext) {

    private val tfLiteInterpreter: Interpreter
    val _tfLiteOptions: Interpreter.Options
    val tfLiteOptions
        get() = _tfLiteOptions


    init {
        val tmpFile = File(appContext.filesDir, subDirWithName)
        tmpFile.createNewFileWithDirIfNotExist()
        tmpFile.writeBytes(readBytes())
        tfLiteInterpreter = Interpreter(tmpFile)
        _tfLiteOptions = Interpreter.Options()
    }


    fun predict(input: FloatArray, output: FloatArray) {
        val inputArray = arrayOf(input)
        val outputArray = arrayOf(output)
        tfLiteInterpreter.run(inputArray, outputArray)
    }


}