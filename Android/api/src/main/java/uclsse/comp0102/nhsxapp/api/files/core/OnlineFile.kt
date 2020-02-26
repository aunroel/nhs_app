package uclsse.comp0102.nhsxapp.api.files.core

import android.content.Context
import uclsse.comp0102.nhsxapp.api.extension.formatSubDir
import uclsse.comp0102.nhsxapp.api.files.core.online.HttpClient
import uclsse.comp0102.nhsxapp.api.files.core.record.ChangeRecorder
import java.io.File
import java.net.URL

open class OnlineFile(onHost: URL, subDirWithName: String, appContext: Context) {

    private val httpClient: HttpClient = HttpClient(onHost)
    private val changeRecorder: ChangeRecorder = ChangeRecorder(appContext, subDirWithName)
    private val subDir = subDirWithName.substringBeforeLast('/').formatSubDir()
    private val fileName: String = subDirWithName.substringAfterLast('/')

    protected val localCopy: File = File(appContext.filesDir, subDirWithName)


    val isModifiedSinceLastUpload: Boolean
        get() = changeRecorder.lastUploadTime < changeRecorder.lastModifiedTime

    init {
        val parentDir = localCopy.parentFile
        if (!parentDir.exists())
            parentDir.mkdirs()
        if (!localCopy.exists())
            localCopy.createNewFile()
    }

    fun readBytes(): ByteArray {
        return localCopy.readBytes()
    }


    fun writeBytes(bytes: ByteArray) {
        localCopy.writeBytes(bytes)
        changeRecorder.lastModifiedTime = System.currentTimeMillis()
    }


    fun update() {
        val tmpData = httpClient.download(subDir, fileName)
        localCopy.writeBytes(tmpData)
        changeRecorder.lastUpdateTime = System.currentTimeMillis()
    }


    fun upload() {
        val tmpData = localCopy.readBytes()
        httpClient.upload(tmpData, subDir, fileName)
        changeRecorder.lastUploadTime = System.currentTimeMillis()
    }

}