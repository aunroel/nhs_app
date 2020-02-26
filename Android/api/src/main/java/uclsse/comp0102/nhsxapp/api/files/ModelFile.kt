package uclsse.comp0102.nhsxapp.api.files

import android.content.Context
import org.tensorflow.lite.Interpreter
import uclsse.comp0102.nhsxapp.api.files.core.OnlineFile
import java.net.URL


class ModelFile(onHost: URL, subDirWithName: String, appContext: Context) :
    OnlineFile(onHost, subDirWithName, appContext) {

    private val tfLiteInterpreter: Interpreter
    val _tfLiteOptions: Interpreter.Options
    val tfLiteOptions
        get() = _tfLiteOptions


    init {
        update()
        tfLiteInterpreter = Interpreter(localCopy)
        _tfLiteOptions = Interpreter.Options()
    }


    fun predict(input: FloatArray, output: FloatArray) {
        val inputArray = arrayOf(input)
        val outputArray = arrayOf(output)
        tfLiteInterpreter.run(inputArray, outputArray)
    }


}