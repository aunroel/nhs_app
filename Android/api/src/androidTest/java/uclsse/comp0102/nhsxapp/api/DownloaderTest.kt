package uclsse.comp0102.nhsxapp.api

import android.content.Context
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import uclsse.comp0102.nhsxapp.api.repository.NhsFileRepository

@RunWith(AndroidJUnit4::class)
class DownloaderTest {

    private var file: OnlineDataWrapper? = null

    private lateinit var context: Context
    private lateinit var nhsRepository: NhsFileRepository

    @Before
    fun setUp() {
        context = getApplicationContext<Context>()
        nhsRepository = NhsFileRepository(context)
    }

    @Test
    fun testAdd() {

    }

    @Test
    fun testDownloadedData() {
        assertThat(file).isNotNull()
        file.update()
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
        if (file != null && file!!.exists()) file.delete()
    }

}
