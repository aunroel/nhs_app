package uclsse.comp0102.nhsxapp.api.files

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth
import org.junit.After
import org.junit.Before
import org.junit.Test
import uclsse.comp0102.nhsxapp.api.extension.toURL
import java.net.URL

class RegistrationFileTest {

    companion object{
        const val TEST_HOST_ADDRESS_STR: String =
            "http://10.0.2.2:5000/"
        const val TEST_REGISTER_FILE_NAME_AND_DIR:String =
            "/node "
    }

    private lateinit var testRegisterFile: RegistrationFile

    private val hostAddress: URL = TEST_HOST_ADDRESS_STR.toURL()
    private val appContext: Context = ApplicationProvider.getApplicationContext()

    @Before
    fun setUp() {
        testRegisterFile = RegistrationFile(hostAddress, TEST_REGISTER_FILE_NAME_AND_DIR, appContext)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getUID() {
        val id = testRegisterFile.uID
        Truth.assertThat(id).isNotEmpty()
    }
}