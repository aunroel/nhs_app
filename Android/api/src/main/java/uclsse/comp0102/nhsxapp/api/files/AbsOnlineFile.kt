package uclsse.comp0102.nhsxapp.api.files

import android.content.Context
import android.util.Log
import uclsse.comp0102.nhsxapp.api.files.local.LocalRecord
import java.io.IOException
import java.net.URL

abstract class AbsOnlineFile(
    onHost: URL, subDirWithName: String, appContext: Context
) {

    private val localRecord:LocalRecord = LocalRecord(subDirWithName, appContext)

    val lastModifiedTime: Long
        get() = localRecord.lastModifiedTime

    val lastUploadTime: Long
        get() = localRecord.lastUploadTime

    val lastUpdateTime: Long
        get() = localRecord.lastUpdateTime

    fun readBytes(): ByteArray {
        return localRecord.content
    }


    fun writeBytes(bytes: ByteArray) {
        localRecord.content = bytes
        localRecord.lastModifiedTime = System.currentTimeMillis()
    }

    @Throws(IOException::class)
    protected abstract fun updateCore()

    fun update() {
        try{
            updateCore()
            localRecord.lastUpdateTime = System.currentTimeMillis()
        }catch (e: IOException){
            e.printStackTrace()
            Log.d("OnlineFie.update", "update fail + ${e.message}")
        }
    }

    @Throws(IOException::class)
    protected abstract fun uploadCore()

    fun upload() {
        try{
            uploadCore()
            localRecord.lastUpdateTime = System.currentTimeMillis()
        }catch (e: IOException){
            e.printStackTrace()
            Log.d("OnlineFie.upload", "upload fail + ${e.message}")
        }
    }


}