package uclsse.comp0102.nhsxapp.api

import android.content.Context
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import uclsse.comp0102.nhsxapp.api.repository.files.OnlineFile
import java.net.URI

@RunWith(AndroidJUnit4::class)
class DownloaderTest {

    private var file: OnlineFile? = null

    private val context = getApplicationContext<Context>()
    private val onlineUrl = URI.create("http://10.0.2.2:5000/file/")
    private val localUri = context.filesDir.toURI()
    private val name = "test.txt"
    private val contentStr = "TEST FILE!!!"

    @Before
    fun setUp() {
        file = OnlineFile(onlineUrl, localUri, name)
    }

    @Test
    fun testFilePath() {
        assertThat(file?.path).isEqualTo(localUri.path + name)
    }

    @Test
    fun testDownloadedData() {
        assertThat(file).isNotNull()
        file!!.downloadOnlineVersion()
        assertThat(file!!.isFile).isTrue()
        assertThat(file!!.readText()).isEqualTo(contentStr)
    }

    @Test
    fun testUploadData() {
        assertThat(file).isNotNull()
        val newData = "NEW FILE!!!"
        file!!.downloadOnlineVersion()
        file!!.writeText(newData)
        file!!.upload()
        file!!.downloadOnlineVersion()
        assertThat(file!!.readLines().toString()).isEqualTo("[$newData]")

    }


    @After
    fun tear() {
        if (file != null && file!!.exists()) file?.delete()
    }

}
