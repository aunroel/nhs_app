package uclsse.comp0102.nhsxapp.api.files.online

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ServerConnector {
    @Streaming
    @GET
    fun download(
        @Url fileUrl: String
    ): Call<ResponseBody>


    @Multipart
    @POST
    fun uploadFile(
        @Part file: MultipartBody.Part,
        @Url uploadUrl: String
    ): Call<ResponseBody>

    @POST
    fun postFields(
        @Body fields: RequestBody,
        @Url uploadUrl: String
    ): Call<ResponseBody>
}