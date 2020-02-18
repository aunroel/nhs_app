package uclsse.comp0102.nhsxapp.api.tools

import android.util.Log
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.*
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

    private val connector: ServerConnector

    init {
        val pathDirectory = File(localPath)
        if (!pathDirectory.exists()) {
            pathDirectory.mkdirs()
        }
        if (!this.exists()) this.createNewFile()
        connector = Retrofit.Builder()
            .baseUrl(onlinePath.toURL())
            .build()
            .create(ServerConnector::class.java)
    }

    open fun pull(fromDir: String = "") {
        try {
            val dirWithFileName = "$fromDir/$name".replace("//", "/").removePrefix("/")
            val response = connector.download(dirWithFileName).execute()
            if (!response.isSuccessful) throw IOException("updateLocalCopy: isNotSuccessful")
            val bytes = response.body()?.bytes() ?: throw IOException("updateLocalCopy:isEmptyBody")
            writeBytes(bytes)
        }catch(t: IOException){
            t.printStackTrace()
            Log.e("LOG:DownloadManager", "updateLocalCopy: ${t.message}")
            throw t
        }
    }


    open fun push(toDir: String = "") {
        val octetStreamType = "application/octet-stream"
        val requestBody = RequestBody.create(MediaType.parse(octetStreamType), this)
        val filePart = MultipartBody.Part.createFormData("file", name, requestBody)
        try {
            val dir = toDir.removePrefix("/")
            val response = connector.upload(filePart, dir).execute()
            if (!response.isSuccessful) throw IOException("uploadLocalCopy: isNotSuccessful")
        }catch (t: IOException){
            t.printStackTrace()
            Log.e("LOG:DownloadManager", "uploadLocalCopy: ${t.message}")
            throw t
        }
    }

}

