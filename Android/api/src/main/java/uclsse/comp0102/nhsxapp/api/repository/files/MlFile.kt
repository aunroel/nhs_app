package uclsse.comp0102.nhsxapp.api.repository.files

import android.content.Context
import org.tensorflow.lite.Interpreter
import uclsse.comp0102.nhsxapp.api.repository.database.BinaryData
import java.net.URL
import java.nio.ByteBuffer

class MlFile(onHost: URL, appContext: Context, core: BinaryData) :
    OnlineFile(onHost, appContext, core) {

    private val tfLiteInterpreter: Interpreter
    private val _tfLiteOptions: Interpreter.Options

    init {
        val byteBuffer = ByteBuffer.wrap(readBytes())
        tfLiteInterpreter = Interpreter(byteBuffer)
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