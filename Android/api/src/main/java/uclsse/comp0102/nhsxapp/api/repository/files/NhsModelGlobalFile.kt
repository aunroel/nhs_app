package uclsse.comp0102.nhsxapp.api.repository.files

import org.tensorflow.lite.Interpreter
import java.net.URI

open class NhsModelGlobalFile(
    onlinePath: URI,
    localPath: URI,
    fileName: String
) : GlobalFile(onlinePath, localPath, fileName) {

    private var tfLiteInterpreter: Interpreter? = null
    private val _tfLiteOptions: Interpreter.Options = Interpreter.Options()

    val tfLiteOptions
        get() = _tfLiteOptions

    override fun downloadOnlineVersion(fromDir: String) {
        super.downloadOnlineVersion(fromDir)
        tfLiteInterpreter?.close()
        tfLiteInterpreter = Interpreter(this)
    }

    open fun predict(input: FloatArray, output: FloatArray) {
        val inputArray = arrayOf(input)
        val outputArray = arrayOf(output)
        tfLiteInterpreter?.run(inputArray, outputArray)
    }


}