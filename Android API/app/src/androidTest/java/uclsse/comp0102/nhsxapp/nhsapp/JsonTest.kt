package uclsse.comp0102.nhsxapp.nhsapp

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import uclsse.comp0102.nhsx.android.tools.JsonGlobalFile
import java.net.URI


class JsonTest {

    private lateinit var file: JsonGlobalFile
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val onlineUrl = URI.create("http://10.0.2.2:5000/static/")
    private val localUri = context.filesDir.toURI()
    private val name = "test.json"

    private val testStr_1 = """
        {"byte":1,"double":2.1,"float":3.1,"int":4,"long":5,"short":6}
    """.trimIndent()

    private val testStr_2 = """
        {"byte":2,"double":3.2,"float":4.2,"int":5,"long":6,"short":7}
    """.trimIndent()

    private val testStr_1Plus2 = """
        {"byte":3,"double":5.3,"float":7.3,"int":9,"long":11,"short":13}
    """.trimIndent()

    private val testData_1 = object {
        val byte: Byte = 1
        val double: Double = 2.1
        val float: Float = 3.1f
        val int: Int = 4
        val long: Long = 5
        val short: Short = 6
    }

    private val testData_2 = object {
        val byte: Byte = 2
        val double: Double = 3.2
        val float: Float = 4.2f
        val int: Int = 5
        val long: Long = 6
        val short: Short = 7
    }

    @Before
    fun setUp() {
        file = JsonGlobalFile(onlineUrl, localUri, name)
    }

    @After
    fun tearDown() {
        if (file.exists()) file.delete()
    }

    @Test
    fun testStoreDataAndOverwriteDuplication() {
        file.storeDataAndOverwriteDuplication(testData_1)
        assertThat(file.readText()).isEqualTo(testStr_1)
        file.storeDataAndOverwriteDuplication(testData_2)
        assertThat(file.readText()).isEqualTo(testStr_2)
    }

    @Test
    fun testStoreDataAndAccumulateDuplication() {
        file.storeDataAndOverwriteDuplication(testData_1)
        file.storeDataAndAccumulateDuplication(testData_2)
        assertThat(file.readText()).isEqualTo(testStr_1Plus2)
    }


}