package uclsse.comp0102.nhsx.android.tools.net

import android.util.Log
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import java.io.File
import java.net.URL


class DownloadManager (
    private val url: URL,
    private val toLocalFile: File
){

    private interface DownloadService{
        @GET("README.md")
        fun download(): Call<ResponseBody>
    }

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(url)
        .build()

    private val service: DownloadService by lazy {
        retrofit.create(
            DownloadService::class.java)
    }

    fun download() = service.download().enqueue(object : Callback<ResponseBody>{
        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            Log.d("DOWNLOAD", t.message.toString())
        }

        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            if (response.isSuccessful) {
                val bytes = response.body()?.bytes()
                if (bytes != null)
                    toLocalFile.outputStream().write(bytes)
                toLocalFile.outputStream().close()
            }
        }
    })


}

