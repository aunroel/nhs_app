package uclsse.comp0102.nhsx.android.works.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://raw.githubusercontent.com/aooXu/Lecture/master/"

interface Downloader{
    @GET("realestate")
    fun downloadFile(): Call<ResponseBody>
}

object DownloadAPI{
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    val DownloadService: Downloader by lazy {
        retrofit.create(DownloadService::class.java)
    }
}