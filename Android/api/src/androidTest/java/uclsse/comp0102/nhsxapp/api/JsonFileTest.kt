package uclsse.comp0102.nhsxapp.api

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import uclsse.comp0102.nhsxapp.api.files.JsonFile
import uclsse.comp0102.nhsxapp.api.files.NhsFileSystem
import kotlin.math.roundToInt

@RunWith(AndroidJUnit4::class)
class JsonFileTest {

    private val nhsRepository: NhsFileSystem
    private val firstFileDirWithName: String
    private val secondFileDirWithName: String
    private val combinedFileDirWithName: String

    private var firstJsonFile: JsonFile? = null
    private var secondJsonFile: JsonFile? = null
    private var combinedJsonFile: JsonFile? = null


    init {
        val context = ApplicationProvider.getApplicationContext<Context>()
        nhsRepository = NhsFileSystem(context)
        firstFileDirWithName = context.getString(R.string.TEST_FIRST_JSON_FILE_PATH)
        secondFileDirWithName = context.getString(R.string.TEST_SECOND_JSON_FILE_PATH)
        combinedFileDirWithName = context.getString(R.string.TEST_COMBINE_JSON_FILE_PATH)
    }

    @Before
    fun setUp() {
        firstJsonFile = nhsRepository.access(
            JsonFile::class.java,
            firstFileDirWithName
        )
        secondJsonFile = nhsRepository.access(
            JsonFile::class.java,
            secondFileDirWithName
        )
        combinedJsonFile = nhsRepository.access(
            JsonFile::class.java,
            combinedFileDirWithName
        )
    }

    @After
    fun tearDown() {
        nhsRepository.clearAllLocalCache()
        firstJsonFile = null
        secondJsonFile = null
    }

    @Test
    fun testTo() {
        val actual = firstJsonFile!!.to(TestJsonFormat::class.java)
        val expected = TestJsonFormat(byte = 1, double = 2.1, float = 3.1f, int = 4, long = 5, short = 6)
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun testStoreDataAndOverwriteDuplication() {
        val expected = secondJsonFile!!.to(TestJsonFormat::class.java)
        val first = firstJsonFile!!
        first.storeAndOverwrite(expected)
        val actual = first.to(TestJsonFormat::class.java)
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun testStoreDataAndAccumulateDuplication() {
        val secondDataObject = secondJsonFile!!.to(TestJsonFormat::class.java)
        val first = firstJsonFile!!
        first.storeAndAccumulate(secondDataObject)
        val actual = first.to(TestJsonFormat::class.java)
        val expected = combinedJsonFile!!.to(TestJsonFormat::class.java)
        assertThat(actual).isEqualTo(expected)
    }

    data class TestJsonFormat(
        val byte: Byte = 0,
        val double: Double = 0.0,
        val float: Float = 0.0f,
        val int: Int = 0,
        val long: Long = 0,
        val short: Short = 0
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as TestJsonFormat

            if (byte != other.byte) return false
            if (double.roundToInt() != other.double.roundToInt()) return false
            if (float.roundToInt() != other.float.roundToInt()) return false
            if (int != other.int) return false
            if (long != other.long) return false
            if (short != other.short) return false

            return true
        }

        override fun hashCode(): Int {
            var result = int
            result = 31 * result + double.roundToInt().hashCode()
            result = 31 * result + float.roundToInt().hashCode()
            result = 31 * result + int
            result = 31 * result + long.hashCode()
            result = 31 * result + short
            return result
        }

    }

}