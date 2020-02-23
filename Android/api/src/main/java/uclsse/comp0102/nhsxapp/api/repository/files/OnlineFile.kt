package uclsse.comp0102.nhsxapp.api.repository.files

import android.content.Context
import android.util.Log
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.*
import uclsse.comp0102.nhsxapp.api.extension.formatSubDir
import uclsse.comp0102.nhsxapp.api.repository.database.BinaryData
import uclsse.comp0102.nhsxapp.api.repository.database.BinaryDataBase
import java.io.IOException
import java.net.URL

open class OnlineFile(onHost: URL, subDirWithName: String, appContext: Context) {

    companion object {
        private var flyWeightDatabase: BinaryDataBase? = null
        private val flyWeighConnectors: MutableMap<String, ServerConnector> = mutableMapOf()
        @Synchronized
        private fun getDatabase(appContext: Context): BinaryDataBase {
            flyWeightDatabase = flyWeightDatabase
                ?: BinaryDataBase.getInstance(appContext)
            return flyWeightDatabase!!
        }

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
    private val database: BinaryDataBase
    private val binaryData: BinaryData

    val isDirty: Boolean
        get() = binaryData.lastModifiedTime > binaryData.lastUploadTime

    init {
        connector = getConnector(onHost)
        database = getDatabase(appContext)
        val formattedSubSir = subDirWithName.formatSubDir()
        var tmpData = database.dataAccessor.get(formattedSubSir)
        if (tmpData == null) {
            tmpData = BinaryData(formattedSubSir)
            tmpData.data = downloadCore(formattedSubSir)
            database.dataAccessor.insert(tmpData)
        }
        binaryData = tmpData
    }


    fun readBytes(): ByteArray {
        return binaryData.data
    }


    fun writeBytes(bytes: ByteArray) {
        binaryData.data = bytes
        binaryData.lastModifiedTime = System.currentTimeMillis()
        database.dataAccessor.update(binaryData)
    }


    fun update(): Boolean {
        return try {
            binaryData.data = downloadCore(binaryData.subDirWithFileName)
            binaryData.lastModifiedTime = System.currentTimeMillis()
            database.dataAccessor.update(binaryData)
            true
        } catch (t: IOException) {
            Log.e("LOG:DownloadManager", "updateLocalCopy: ${t.message}")
            false
        }
    }


    fun upload(): Boolean {
        return try {
            uploadCore(binaryData)
            binaryData.lastUploadTime = System.currentTimeMillis()
            database.dataAccessor.update(binaryData)
            true
        } catch (t: IOException) {
            Log.e("LOG:DownloadManager", "uploadLocalCopy: ${t.message}")
            false
        }
    }


    private fun downloadCore(fromDirWithFileName: String): ByteArray {
        val response = connector.download(fromDirWithFileName).execute()
        if (!response.isSuccessful) throw IOException("updateLocalCopy: isNotSuccessful")
        return response.body()?.bytes()
            ?: throw IOException("updateLocalCopy:isEmptyBody")
    }


    private fun uploadCore(newBinaryData: BinaryData) {
        val octetStreamType = "application/octet-stream"
        val requestBody = RequestBody.create(
            MediaType.parse(octetStreamType), newBinaryData.data
        )
        val filePart = MultipartBody.Part.createFormData(
            "file", newBinaryData.name, requestBody
        )
        val response = connector.upload(filePart, newBinaryData.subDir).execute()
        if (!response.isSuccessful)
            throw IOException("uploadLocalCopy: ${response.message()}")
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