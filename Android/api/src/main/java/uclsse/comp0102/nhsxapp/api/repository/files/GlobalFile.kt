package uclsse.comp0102.nhsxapp.api.repository.files

import android.util.Log
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.*
import uclsse.comp0102.nhsxapp.api.extension.formatUriSubDir
import java.io.File
import java.io.IOException
import java.net.URI


open class GlobalFile(
    onlinePath: URI,
    localPath: URI,
    fileName: String
) : File(localPath.path, fileName) {

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

    private var lastUploadTime: Long

    private val connector: ServerConnector


    init {
        lastUploadTime = 0
        connector = Retrofit.Builder()
            .baseUrl(onlinePath.toURL())
            .build()
            .create(ServerConnector::class.java)
    }

    fun isDirty(): Boolean = lastUploadTime <= lastModified()

    open fun downloadOnlineVersion(fromDir: String = "") {
        try {
            val dirWithFileName = "$fromDir/$name".formatUriSubDir()
            val response = connector.download(dirWithFileName).execute()
            if (!response.isSuccessful) throw IOException("updateLocalCopy: isNotSuccessful")
            val bytes = response.body()?.bytes()
                ?: throw IOException("updateLocalCopy:isEmptyBody")
            writeBytes(bytes)
        }catch(t: IOException){
            t.printStackTrace()
            Log.e("LOG:DownloadManager", "updateLocalCopy: ${t.message}")
            throw t
        }
    }


    open fun uploadLocalVersion(toDir: String = "") {
        val octetStreamType = "application/octet-stream"
        val requestBody = RequestBody.create(MediaType.parse(octetStreamType), this.readBytes())
        val filePart = MultipartBody.Part.createFormData("file", name, requestBody)
        try {
            val dir = toDir.removePrefix("/")
            val response = connector.upload(filePart, dir).execute()
            if (!response.isSuccessful) throw IOException("uploadLocalCopy: ${response.message()}")
            lastUploadTime = System.currentTimeMillis()
        }catch (t: IOException){
            t.printStackTrace()
            Log.e("LOG:DownloadManager", "uploadLocalCopy: ${t.message}")
            throw t
        }
    }

}

