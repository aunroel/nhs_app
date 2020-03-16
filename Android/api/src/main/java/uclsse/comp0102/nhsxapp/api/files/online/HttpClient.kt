package uclsse.comp0102.nhsxapp.api.files.online

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import uclsse.comp0102.nhsxapp.api.extension.formatSubDir
import java.io.IOException
import java.net.URL

// The class is a simply encapsulation for the
// retrofit2 library. It is used to upload the
// json string and download the model data
class HttpClient(onHost: URL) {

    // These static field and method are used
    // to implement the flyweight design pattern
    // for reducing the memory space.
    companion object {
        private val FLY_WEIGH_CONNECTORS: MutableMap<String, ServerConnector> = mutableMapOf()
        @Synchronized
        private fun getConnector(hostUrl: URL): ServerConnector {
            val connector = FLY_WEIGH_CONNECTORS[hostUrl.path]
            if (connector != null) return connector
            val newConnector = Retrofit.Builder()
                .baseUrl(hostUrl)
                .build()
                .create(ServerConnector::class.java)
            FLY_WEIGH_CONNECTORS[hostUrl.path] = newConnector
            return newConnector
        }
    }


    private val connector: ServerConnector = getConnector(onHost)

    fun download(fromSubDir: String, fileName: String): ByteArray {
        val subDirWithName = "$fromSubDir/$fileName".formatSubDir()
        val response = connector.download(subDirWithName).execute()
        if (!response.isSuccessful) throw IOException("updateLocalCopy: ${response.message()}")
        return response.body()?.bytes() ?: throw IOException("updateLocalCopy:isEmptyBody")
    }


    fun upload(contents: ByteArray, toSubDir: String, fileName: String) {
        val octetStreamType = "application/octet-stream"
        val requestBody = RequestBody.create(MediaType.parse(octetStreamType), contents)
        val filePart = MultipartBody.Part.createFormData("file", fileName, requestBody)
        val response = connector.upload(filePart, toSubDir).execute()
        if (!response.isSuccessful) throw IOException("uploadLocalCopy: ${response.message()}")
    }

    fun get(fromSubDir: String): ByteArray {
        val response = connector.download(fromSubDir).execute()
        if (!response.isSuccessful) throw IOException("get: ${response.message()}")
        return response.body()?.bytes() ?: throw IOException("get:isEmptyBody")
    }

    fun post(jsonStr: String, toSubDir: String){
        val body = RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            jsonStr
        )
        val response = connector.postFields(body, toSubDir).execute()
        if (!response.isSuccessful) throw IOException("post: ${response.message()}")
    }

}