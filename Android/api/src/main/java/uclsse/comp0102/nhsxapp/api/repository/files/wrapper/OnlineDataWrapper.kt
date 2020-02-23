package uclsse.comp0102.nhsxapp.api.repository.files.wrapper

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


class OnlineDataWrapper(onHost: URL, core: BinaryData) : OnlineAccessible {

    companion object {
        private val flyWeighConnectors: MutableMap<String, ServerConnector> = mutableMapOf()
        @Synchronized
        private fun getConnector(hostUrl: URL): ServerConnector {
            var result = flyWeighConnectors[hostUrl.path]
            result = result
                ?: Retrofit.Builder()
                    .baseUrl(hostUrl)
                    .build()
                    .create(ServerConnector::class.java)
            flyWeighConnectors[hostUrl.path] = result!!
            return result
        }
    }


    private val connector: ServerConnector
    private val binaryData: BinaryData

    init {
        connector = getConnector(onHost)
        binaryData = core
    }

    fun isDirty(): Boolean {
        return binaryData.lastModifiedTime > binaryData.lastUploadTime
        //TODO("EXTEND THE LOGIC")
    }

    override fun update(): Boolean {
        return try {
            val dirWithFileName = binaryData.subDirWithFileName
            val response = connector.download(dirWithFileName).execute()
            if (!response.isSuccessful) throw IOException("updateLocalCopy: isNotSuccessful")
            binaryData.data = response.body()?.bytes()
                ?: throw IOException("updateLocalCopy:isEmptyBody")
            true
        } catch (t: IOException) {
            t.printStackTrace()
            Log.e("LOG:DownloadManager", "updateLocalCopy: ${t.message}")
            false
        }
    }


    override fun upload(): Boolean {
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
            true
        } catch (t: IOException) {
            t.printStackTrace()
            Log.e("LOG:DownloadManager", "uploadLocalCopy: ${t.message}")
            false
        }
    }


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
