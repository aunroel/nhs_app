package uclsse.comp0102.nhsxapp.api.files.core.online

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ServerRetrofitConnector {
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