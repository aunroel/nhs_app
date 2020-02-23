package uclsse.comp0102.nhsxapp.api.repository.files

import android.content.Context
import org.tensorflow.lite.Interpreter
import uclsse.comp0102.nhsxapp.api.extension.createNewFileWithDirIfNotExist
import java.io.File
import java.net.URL


class ModelFile(onHost: URL, subDirWithName: String, appContext: Context) :
    OnlineFile(onHost, subDirWithName, appContext) {

    private val tfLiteInterpreter: Interpreter
    private val _tfLiteOptions: Interpreter.Options

    init {
        val tmpFile = File(appContext.filesDir, subDirWithName)
        tmpFile.createNewFileWithDirIfNotExist()
        tmpFile.writeBytes(readBytes())
        tfLiteInterpreter = Interpreter(tmpFile)
        _tfLiteOptions = Interpreter.Options()
    }

    val tfLiteOptions
        get() = _tfLiteOptions


    fun predict(input: FloatArray, output: FloatArray) {
        val inputArray = arrayOf(input)
        val outputArray = arrayOf(output)
        tfLiteInterpreter.run(inputArray, outputArray)
    }


}