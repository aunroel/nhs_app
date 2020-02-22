package uclsse.comp0102.nhsxapp.api.repository.files

import android.util.Log
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.*
import uclsse.comp0102.nhsxapp.api.repository.database.BinaryData
import java.io.IOException
import java.net.URL


open class OnlineFile(onHost: URL, core: BinaryData) {

    companion object {
        private val flyWeigher: MutableMap<String, ServerConnector> = mutableMapOf()
        private fun getConnector(hostUrl: URL): ServerConnector {
            var result = flyWeigher[hostUrl.path]
            result = result
                ?: Retrofit.Builder()
                    .baseUrl(hostUrl)
                    .build()
                    .create(ServerConnector::class.java)
            flyWeigher[hostUrl.path] = result!!
            return result
        }
    }

    var whenChangeOccurredOn: ((data: BinaryData) -> Unit) = {}

    private val connector: ServerConnector
    private val binaryData: BinaryData

    init {
        connector = getConnector(onHost)
        binaryData = core
    }

    fun readBytes(): ByteArray {
        return binaryData.data
    }

    fun writeBytes(newData: ByteArray) {
        binaryData.data = newData
        binaryData.lastModifiedTime = System.currentTimeMillis()
        whenChangeOccurredOn(binaryData)
    }

    fun isDirty(): Boolean {
        return binaryData.lastModifiedTime > binaryData.lastUploadTime
        //TODO("EXTEND THE LOGIC")
    }

    open fun download(isAllowedToOverwriteDirtyFile: Boolean = true): Boolean {
        return try {
            if (isDirty() && !isAllowedToOverwriteDirtyFile)
                throw UnsupportedOperationException("overwrite a dirty file")
            val dirWithFileName = binaryData.subDirWithFileName
            val response = connector.download(dirWithFileName).execute()
            if (!response.isSuccessful) throw IOException("updateLocalCopy: isNotSuccessful")
            binaryData.data = response.body()?.bytes()
                ?: throw IOException("updateLocalCopy:isEmptyBody")
            whenChangeOccurredOn(binaryData)
            true
        } catch (t: IOException) {
            t.printStackTrace()
            Log.e("LOG:DownloadManager", "updateLocalCopy: ${t.message}")
            false
        }
    }
    //TODO("Make it return a boolean value")


    open fun upload(): Boolean {
        return try {
            val octetStreamType = "application/octet-stream"
            val fileName = binaryData.subDirWithFileName.substringAfterLast('/')
            val subDir = binaryData.subDirWithFileName.substringBeforeLast('/')
            val requestBody = RequestBody.create(
                MediaType.parse(octetStreamType),
                binaryData.data
            )
            val filePart = MultipartBody.Part.createFormData(
                "file",
                fileName,
                requestBody
            )
            val response = connector.upload(filePart, subDir).execute()
            if (!response.isSuccessful)
                throw IOException("uploadLocalCopy: ${response.message()}")
            binaryData.lastUploadTime = System.currentTimeMillis()
            whenChangeOccurredOn(binaryData)
            true
        } catch (t: IOException) {
            t.printStackTrace()
            Log.e("LOG:DownloadManager", "uploadLocalCopy: ${t.message}")
            false
        }
    }
    //TODO("Make it return a boolean value")


    private interface ServerConnector {
        @Streaming
        @GET
        fun download(
            @Url fileUrl: String
        ): Call<ResponseBody>


        @Multipart
        @POST
        fun upload(
            @Part file: MultipartBody.Part,
            @Url uploadUrl: String
        ): Call<ResponseBody>
    }
}
