package uclsse.comp0102.nhsxapp.api

import android.content.Context
import uclsse.comp0102.nhsxapp.api.extension.formatSubDir
import uclsse.comp0102.nhsxapp.api.extension.toURL
import uclsse.comp0102.nhsxapp.api.files.JsonFile
import uclsse.comp0102.nhsxapp.api.files.ModelFile
import uclsse.comp0102.nhsxapp.api.files.RegistrationFile
import uclsse.comp0102.nhsxapp.api.files.finder.OnlineFileFinder

class NhsFileRepository(
    private val appContext: Context
){
    private val uID: String
    private val fileFinder: OnlineFileFinder

    init {
        val onHost = appContext.getString(R.string.HOST_ADDRESS).toURL()
        fileFinder = OnlineFileFinder(onHost, appContext)
        // Access the uID of the application by the "RegistrationFile" class
        val registrationFile = fileFinder.access(
            RegistrationFile::class.java,
            appContext.getString(R.string.REGISTER_FILE_PATH)
        )
        uID = registrationFile.uID
    }

    // Access the json file
    fun getJsonFile(): JsonFile {
        val jsonFileSubDir = appContext.getString(R.string.JSON_FILE_SUB_DIR)
        val jsonPath = "$jsonFileSubDir/$uID".formatSubDir()
        return fileFinder.access(JsonFile::class.java, jsonPath)
    }

    // Access model file
    fun getModelFile() : ModelFile {
        val tflFileSubDir = appContext.getString(R.string.TFL_FILE_SUB_DIR)
        val modelPath = "$tflFileSubDir/$uID".formatSubDir()
        return fileFinder.access(ModelFile::class.java, modelPath)
    }

}