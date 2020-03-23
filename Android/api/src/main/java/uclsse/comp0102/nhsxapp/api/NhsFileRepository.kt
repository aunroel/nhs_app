package uclsse.comp0102.nhsxapp.api

import android.content.Context
import uclsse.comp0102.nhsxapp.api.extension.formatSubDir
import uclsse.comp0102.nhsxapp.api.extension.toURL
import uclsse.comp0102.nhsxapp.api.files.JsonFile
import uclsse.comp0102.nhsxapp.api.files.ModelFile
import uclsse.comp0102.nhsxapp.api.files.RegistrationFile
import uclsse.comp0102.nhsxapp.api.files.finder.OnlineFileFinder

/**
 * # 1. Description:
 * The class is a tool class for the NhsAPI class and NhsSynchroniser class. It is used as a factory
 * class for access the instance of OnlineFile class.
 *
 * # 2. Public Methods:
 * fun getJsonFile(): JsonFile
 * fun getModelFile() : ModelFile
 */
class NhsFileRepository private constructor(appContext: Context){

    /**
     * ## Static variables: They are used to implement the singleton design pattern.
     * */
    companion object {
        private var instance: NhsFileRepository? = null
        fun getInstance(appContext: Context): NhsFileRepository{
            instance = instance ?: NhsFileRepository(appContext)
            return instance!!
        }

    }

    /**
     * ## Non-static variables: the files used in the projects.
     * */
    private val jsonFile: JsonFile
    private val modelFile: ModelFile

    init {
        val onHost = appContext.getString(R.string.HOST_ADDRESS).toURL()
        val fileFinder = OnlineFileFinder(onHost, appContext)
        // Access the uID of the application by the "RegistrationFile" class
        val registrationFile = fileFinder.access(
            RegistrationFile::class.java,
            appContext.getString(R.string.REGISTER_FILE_PATH)
        )
        val uID = registrationFile.uID

        val jsonFileSubDir = appContext.getString(R.string.JSON_FILE_SUB_DIR)
        val jsonPath = "$jsonFileSubDir/$uID".formatSubDir()
        jsonFile = fileFinder.access(JsonFile::class.java, jsonPath)

        val modelFileSubDir = appContext.getString(R.string.TFL_FILE_SUB_DIR)
        val modelPath = "$modelFileSubDir/$uID".formatSubDir()
        modelFile = fileFinder.access(ModelFile::class.java, modelPath)
    }

    /**
     * ## Description: get the json file used by the application
     *
     * ## ReturnValue: The instance of the JsonFile Class
     */
    fun getJsonFile(): JsonFile {
        return jsonFile
    }

    /**
     * ## Description: get the TensorFlow Lite model file used by the application
     *
     * ## ReturnValue: The instance of the ModelFile Class
     */
    fun getModelFile() : ModelFile {
        return modelFile
    }

}