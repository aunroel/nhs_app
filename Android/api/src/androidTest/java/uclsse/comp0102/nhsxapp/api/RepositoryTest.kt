package uclsse.comp0102.nhsxapp.api

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import uclsse.comp0102.nhsxapp.api.repository.NhsFileRepository
import uclsse.comp0102.nhsxapp.api.repository.files.JsonFile
import uclsse.comp0102.nhsxapp.api.repository.files.MlFile
import java.io.File
import java.net.URI

class RepositoryTest {

    private val context: Context
    private val onlineUri: URI
    private val localUri: URI
    private val repository: NhsFileRepository


    init {
        context = ApplicationProvider.getApplicationContext<Context>()
        onlineUri = URI.create("http://10.0.2.2:5000/")
        localUri = File(context.filesDir, "repository").toURI()

        NhsFileRepository.setUri(onlineUri, localUri)
        repository = NhsFileRepository.instance!!

    }

    @Test
    fun testDownload() {
        val newGlobalFile = repository.FileBuilder()
            .setFileName("test.txt")
            .setFileSubDir("file")
            .build(OnlineDataWrapper::class.java)

        val newJsonFile = repository.FileBuilder()
            .setFileName("test.json")
            .setFileSubDir("file")
            .build(JsonFile::class.java)

        val newTfFile = repository.FileBuilder()
            .setFileName("test.tflite")
            .setFileSubDir("file")
            .build(MlFile::class.java)

        repository.add(newGlobalFile)
        repository.add(newJsonFile)
        repository.add(newTfFile)

        repository.pull()


        assertThat(newGlobalFile.readText()).isEqualTo("TEST FILE!!!")
        val expectStr = """
             {"byte":3,"double":5.3,"float":7.2999997,"int":9,"long":11,"short":13}
             """.trimIndent()
        assertThat(repository.access("test.json", "file").readText()).isEqualTo(expectStr)

    }
}