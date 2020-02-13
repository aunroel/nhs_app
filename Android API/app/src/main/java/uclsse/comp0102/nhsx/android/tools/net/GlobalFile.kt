package uclsse.comp0102.nhsx.android.tools.net

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


class GlobalFile (
    private val onlineURI: URI,
    private val localURI: URI,
    private val fileName: String
) : File(localURI.path, fileName) {

    init {
        val pathDirectory= File(localURI)
        if(!pathDirectory.exists()){ pathDirectory.mkdirs() }
        if(!this.exists()) this.createNewFile()
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
            @Url uploadUrl: String = "upload"
        ): Call<ResponseBody>

    }

    private val connector = Retrofit.Builder()
        .baseUrl(onlineURI.toURL())
        .build()
        .create(ServerConnector::class.java)

    fun updateLocalCopy(){
        try {
            val response = connector.download(fileName).execute()
            if (!response.isSuccessful) throw IOException("updateLocalCopy: isNotSuccessful")
            val bytes = response.body()?.bytes() ?: throw IOException("updateLocalCopy: isEmptyBody")
            writeBytes(bytes)
        }catch(t: IOException){
            t.printStackTrace()
            Log.e("LOG:DownloadManager", "updateLocalCopy: ${t.message}")
        }
    }


    fun uploadLocalCopy(toDirectory: String = "") {
        val octetStreamType = "application/octet-stream"
        val fileStream = RequestBody.create(MediaType.parse(octetStreamType), this)
        val filePart = MultipartBody.Part.createFormData("file", fileName, fileStream)
        try {
            val response =  connector.upload(filePart, toDirectory).execute()
            if (!response.isSuccessful) throw IOException("uploadLocalCopy: isNotSuccessful")
        }catch (t: IOException){
            Log.e("LOG:DownloadManager", "uploadLocalCopy: ${t.message}")
        }
    }

}

