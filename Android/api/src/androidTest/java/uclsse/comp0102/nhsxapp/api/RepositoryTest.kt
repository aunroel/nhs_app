package uclsse.comp0102.nhsxapp.api

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import uclsse.comp0102.nhsxapp.api.repository.NhsRepository
import uclsse.comp0102.nhsxapp.api.repository.files.GlobalFile
import uclsse.comp0102.nhsxapp.api.repository.files.JsonGlobalFile
import uclsse.comp0102.nhsxapp.api.repository.files.NhsModelGlobalFile
import java.io.File
import java.net.URI

class RepositoryTest {

    private val context: Context
    private val onlineUri: URI
    private val localUri: URI
    private val repository: NhsRepository


    init {
        context = ApplicationProvider.getApplicationContext<Context>()
        onlineUri = URI.create("http://10.0.2.2:5000/")
        localUri = File(context.filesDir, "repository").toURI()

        NhsRepository.setUri(onlineUri, localUri)
        repository = NhsRepository.instance!!

    }

    @Test
    fun testDownload() {
        val newGlobalFile = repository.FileBuilder()
            .setFileName("test.txt")
            .setFileSubDir("file")
            .build(GlobalFile::class.java)

        val newJsonFile = repository.FileBuilder()
            .setFileName("test.json")
            .setFileSubDir("file")
            .build(JsonGlobalFile::class.java)

        val newTfFile = repository.FileBuilder()
            .setFileName("test.tflite")
            .setFileSubDir("file")
            .build(NhsModelGlobalFile::class.java)

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