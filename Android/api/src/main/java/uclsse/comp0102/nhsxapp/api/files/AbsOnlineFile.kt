package uclsse.comp0102.nhsxapp.api.files

import android.content.Context
import android.util.Log
import uclsse.comp0102.nhsxapp.api.files.local.LocalRecord
import java.io.IOException
import java.net.URL

/** An abstract class provides a basic framework for storing data,
 * accessing the last modified time, last update time and last upload
 * time of a file.
 * to be implemented by class JsonFile, class ModelFile, and class RegistrationFile
 */
abstract class AbsOnlineFile(
    onHost: URL, subDirWithName: String, appContext: Context
) {

    // the local record of the data.
    private val localRecord:LocalRecord = LocalRecord(subDirWithName, appContext)

    // data in the data record
    val lastModifiedTime: Long
        get() = localRecord.lastModifiedTime
    val lastUploadTime: Long
        get() = localRecord.lastUploadTime
    val lastUpdateTime: Long
        get() = localRecord.lastUpdateTime

    // Functions about the local record
    /** read content
     */
    fun readBytes(): ByteArray {
        return localRecord.content
    }

    /** write content to the record
     * modify the last modified timestamp
     */
    fun writeBytes(bytes: ByteArray) {
        localRecord.content = bytes
        localRecord.lastModifiedTime = System.currentTimeMillis()
    }

    // the abstract functions and the basic function about
    // network data updating and downloading.
    // to be implemented
    @Throws(IOException::class)
    protected abstract fun updateCore()

    /** update the local file
     * modify the last update timestamp
     * if failed show error message
     */
    fun update() :Boolean{
        return try{
            updateCore()
            localRecord.lastUpdateTime = System.currentTimeMillis()
            true
        }catch (e: IOException){
            e.printStackTrace()
            Log.d("OnlineFie.update", "update fail + ${e.message}")
            false
        }
    }

    // to be implemented
    @Throws(IOException::class)
    protected abstract fun uploadCore()

    /** upload the local file to the server
     * modify the last update timestamp
     * if failed show error message
     */
    fun upload(): Boolean{
        return try{
            uploadCore()
            localRecord.lastUpdateTime = System.currentTimeMillis()
            true
        }catch (e: IOException){
            e.printStackTrace()
            Log.d("OnlineFie.upload", "upload fail + ${e.message}")
            false
        }
    }


}