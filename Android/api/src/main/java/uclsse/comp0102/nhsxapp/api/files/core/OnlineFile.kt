package uclsse.comp0102.nhsxapp.api.files.core

import android.content.Context
import uclsse.comp0102.nhsxapp.api.extension.formatSubDir
import uclsse.comp0102.nhsxapp.api.files.core.online.HttpClient
import uclsse.comp0102.nhsxapp.api.files.core.record.RecordHelper
import java.net.URL

open class OnlineFile(onHost: URL, subDirWithName: String, appContext: Context) {

    private val httpClient: HttpClient = HttpClient(onHost)
    private val recordHelper: RecordHelper = RecordHelper(appContext, subDirWithName)
    private val formattedSubDirWithName = subDirWithName.formatSubDir()
    private val subDir = formattedSubDirWithName.substringBeforeLast('/')+"/"
    private val fileName: String = formattedSubDirWithName.substringAfterLast('/')

    val isModifiedSinceLastUpload: Boolean
        get() = recordHelper.lastUploadTime < recordHelper.lastModifiedTime

    init {
        if (recordHelper.lastUpdateTime == 0L) update()
    }

    fun readBytes(): ByteArray {
        return recordHelper.content
    }


    fun writeBytes(bytes: ByteArray) {
        recordHelper.content = bytes
        recordHelper.lastModifiedTime = System.currentTimeMillis()
    }


    fun update() {
        recordHelper.content = httpClient.download(subDir, fileName)
        recordHelper.lastUpdateTime = System.currentTimeMillis()
    }


    fun upload() {
        httpClient.upload(recordHelper.content, subDir, fileName)
        recordHelper.lastUploadTime = System.currentTimeMillis()
    }

    fun timeForLastUpdate():Long{
        return recordHelper.lastUpdateTime
    }

}