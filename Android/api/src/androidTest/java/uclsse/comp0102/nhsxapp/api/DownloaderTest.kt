package uclsse.comp0102.nhsxapp.api

import android.content.Context
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import uclsse.comp0102.nhsxapp.api.tools.GlobalFile
import java.net.URI

@RunWith(AndroidJUnit4::class)
class DownloaderTest {

    private lateinit var file: GlobalFile

    private val context = getApplicationContext<Context>()
    private val onlineUrl = URI.create("http://10.0.2.2:5000/static/")
    private val localUri = context.filesDir.toURI()
    private val name = "test.txt"

    @Before
    fun setUp() {
        file = GlobalFile(
            onlineUrl,
            localUri,
            name
        )
    }

    @Test
    fun testFilePath() {
        assertThat(file.path).isEqualTo(localUri.path + name)
    }

    @Test
    fun testDownloadedData() {
        file.pull()
        assertThat(file.isFile).isTrue()
        assertThat(file.name).isEqualTo("test.txt")
    }

    @Test
    fun testUploadData() {
        val newData = "NEW FILE!!!"
        file.pull()
        file.writeText(newData)
        file.push("/uploader")
        file.pull()
        assertThat(file.readLines().toString()).isEqualTo("[$newData]")

    }


    @After
    fun tear() {
        file.delete()
    }

}
