package uclsse.comp0102.nhsxapp.nhsapp

import android.content.Context
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import uclsse.comp0102.nhsx.android.tools.net.GlobalFile
import java.net.URI

@RunWith(AndroidJUnit4::class)
class DownloadTest {

    private lateinit var file: GlobalFile

    private val context = getApplicationContext<Context>()
    private val onlineUrl = URI.create("http://10.0.2.2:5000/static/")
    private val localUri = context.filesDir.toURI()
    private val name = "test.txt"

    @Before
    fun setUp() {
        file = GlobalFile(onlineUrl, localUri, name)
    }

    @Test
    fun testFilePath() {
        assertThat(file.path).isEqualTo(localUri.path + name)
    }

    @Test
    fun testDownloadedData() {
        file.updateLocalCopy()
        val data = file.readLines().joinToString()
        assertThat(data).isEqualTo("this is a test file")
    }

    @Test
    fun testUploadData() {
        val newText = "This is a new test file!"
        file.writeText(newText)
        file.uploadLocalCopy("/uploader")
        val data = file.readLines().joinToString()
        assertThat(data).isEqualTo(newText)
    }


    @After
    fun tear() {
        file.delete()
    }

}
