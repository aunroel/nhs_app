package uclsse.comp0102.nhsxapp.api.files.core.online

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import uclsse.comp0102.nhsxapp.api.extension.formatSubDir
import java.io.IOException
import java.net.URL

class HttpClient(onHost: URL) {

    companion object {
        private val flyWeighConnectors: MutableMap<String, ServerRetrofitConnector> = mutableMapOf()
        @Synchronized
        private fun getConnector(hostUrl: URL): ServerRetrofitConnector {
            val connector = flyWeighConnectors[hostUrl.path]
            if (connector != null) return connector
            val newConnector = Retrofit.Builder()
                .baseUrl(hostUrl)
                .build()
                .create(ServerRetrofitConnector::class.java)
            flyWeighConnectors[hostUrl.path] = newConnector
            return newConnector
        }
    }

    private val connector: ServerRetrofitConnector = getConnector(onHost)

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

}