package uclsse.comp0102.nhsxapp.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import uclsse.comp0102.nhsxapp.api.files.online.HttpClient
import java.net.URL

@RunWith(AndroidJUnit4::class)
class OnlineFileTest {

    private val httpClient = HttpClient(URL("http://127.0.0.1:5000/"))

    private val uID = "e01a469f-abfd-48a6-864f-4f796613b7c4"
    private val subDirWithName = ""
    private val fileName = "/model"

    @Test
    fun testDownload() {
        httpClient.download(subDirWithName, fileName)
    }

}
