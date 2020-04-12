package uclsse.comp0102.nhsxapp.api.files

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import uclsse.comp0102.nhsxapp.api.extension.toURL
import java.net.URL
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
@MediumTest
class JsonFileTest {

    companion object{
        const val TEST_RANDOM_RANGE_INT: Int = 10000
        const val TEST_HOST_ADDRESS_STR: String = "http://10.0.2.2:5000/"
        const val TEST_INIT_DATA_STR: String = """ { Integer01: 0, String02: "" } """
        const val TEST_DATA_EMPTY_STR: String = ""
        const val TEST_DATA_EMPTY_JSONSTR: String = "{}"
    }
    private lateinit var testJsonFile: JsonFile

    private val hostAddress: URL = TEST_HOST_ADDRESS_STR.toURL()
    private val appContext: Context = ApplicationProvider.getApplicationContext()

    class TestJsonData(
        @JsonFile.JsonData(name = "Integer01")
        val fieldOne: Int = 1,
        @JsonFile.JsonData(name = "String02")
        val fieldTwo: String = "2"
    )

    @Before
    fun setUp() {
        val subDirWithName = "random" + Random(TEST_RANDOM_RANGE_INT).nextInt().toString() + ".json"
        testJsonFile = JsonFile(hostAddress, subDirWithName, appContext)
        testJsonFile.writeStr(TEST_INIT_DATA_STR)
    }

    @After
    fun tearDown() {
        testJsonFile.writeStr(TEST_DATA_EMPTY_JSONSTR)
    }

    @Test
    fun readAndWriteStr() {
        val initialStr = testJsonFile.readStr()
        Truth.assertThat(initialStr).isEqualTo(TEST_INIT_DATA_STR)

        testJsonFile.writeStr(TEST_DATA_EMPTY_STR)
        val emptyStr = testJsonFile.readStr()
        Truth.assertThat(emptyStr).isEqualTo(TEST_DATA_EMPTY_STR)

        testJsonFile.writeStr(TEST_DATA_EMPTY_JSONSTR)
        val emptyJson = testJsonFile.readStr()
        Truth.assertThat(emptyJson).isEqualTo(TEST_DATA_EMPTY_JSONSTR)
    }

    @Test
    fun readObject() {
        val expectedInitial = TestJsonData(0, "")
        val realInitial = testJsonFile.readObject(TestJsonData::class.java)
        Truth.assertThat(expectedInitial.fieldOne).isEqualTo(realInitial.fieldOne)
        Truth.assertThat(expectedInitial.fieldTwo).isEqualTo(realInitial.fieldTwo)

        val expectedEmptyJson = TestJsonData()
        testJsonFile.writeStr(TEST_DATA_EMPTY_JSONSTR)
        val realEmptyJson = testJsonFile.readObject(TestJsonData::class.java)
        Truth.assertThat(expectedEmptyJson.fieldOne).isEqualTo(realEmptyJson.fieldOne)
        Truth.assertThat(expectedEmptyJson.fieldTwo).isEqualTo(realEmptyJson.fieldTwo)

        try {
            testJsonFile.writeStr(TEST_DATA_EMPTY_STR)
            testJsonFile.readObject(TestJsonData::class.java)
            Assert.fail("Throwable was expected to throw.")
        } catch (ignore: TypeCastException) { }

        try {
            testJsonFile.readObject(Any::class.java)
            Assert.fail("Throwable was expected to throw.")
        } catch (ignore: TypeCastException) { }


    }

    @Test
    fun writeObject() {
        val expectedInitial = TestJsonData(0, "")
        testJsonFile.writeObject(expectedInitial)
        val realInitial = testJsonFile.readObject(TestJsonData::class.java)
        Truth.assertThat(expectedInitial.fieldOne).isEqualTo(realInitial.fieldOne)
        Truth.assertThat(expectedInitial.fieldTwo).isEqualTo(realInitial.fieldTwo)

        val expectedAny = TEST_DATA_EMPTY_JSONSTR
        testJsonFile.writeObject(Any())
        val realAny = testJsonFile.readStr()
        Truth.assertThat(expectedAny).isEqualTo(realAny)
    }

}