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
import uclsse.comp0102.nhsxapp.api.repository.files.OnlineFile

@RunWith(AndroidJUnit4::class)
class OnlineFileTest {

    private val nhsRepository: NhsFileRepository
    private val sourceFileDirWithName: String
    private val targetFileDirWithName: String
    private val initialDataInSourceFile: String
    private val initialDataInTargetFile: String

    private var sourceFile: OnlineFile? = null
    private var targetFile: OnlineFile? = null


    init {
        val context = getApplicationContext<Context>()
        nhsRepository = NhsFileRepository(context)
        sourceFileDirWithName = context.getString(R.string.TEST_SRC_TXT_FILE_PATH)
        targetFileDirWithName = context.getString(R.string.TEST_TAR_TXT_FILE_PATH)
        initialDataInSourceFile = context.getString(R.string.TEST_INIT_DATA_IN_SRC_FILE)
        initialDataInTargetFile = context.getString(R.string.TEST_INIT_DATA_IN_TAR_FILE)
    }

    @Before
    fun setUp() {
        sourceFile = nhsRepository.access(
            OnlineFile::class.java,
            sourceFileDirWithName
        )
        targetFile = nhsRepository.access(
            OnlineFile::class.java,
            targetFileDirWithName
        )

    }

    @After
    fun tearDown() {
        nhsRepository.clearAllLocalCache()
        sourceFile = null
        targetFile = null
    }

    @Test
    fun testReadBytes() {
        val src = sourceFile!!
        assertThat(src).isNotNull()
        val txtDataInSourceFile = src.readBytes().toString(Charsets.UTF_8)
        assertThat(txtDataInSourceFile).isEqualTo(initialDataInSourceFile)
        val tar = targetFile!!
        assertThat(tar).isNotNull()
        val txtDataInTargetFile = tar.readBytes().toString(Charsets.UTF_8)
        assertThat(txtDataInTargetFile).isEqualTo(initialDataInTargetFile)
    }

    @Test
    fun testWriteBytes() {
        val src = sourceFile!!
        val tar = targetFile!!
        tar.writeBytes(src.readBytes())
        val newDataFromRepository = nhsRepository.access(
            OnlineFile::class.java,
            targetFileDirWithName
        ).readBytes()
        assertThat(newDataFromRepository).isEqualTo(src.readBytes())
    }

    @Test
    fun testUpdate() {
        val src = sourceFile!!
        val tar = targetFile!!
        tar.writeBytes(src.readBytes())
        tar.update()
        val txtDataInTargetFile = tar.readBytes().toString(Charsets.UTF_8)
        assertThat(txtDataInTargetFile).isEqualTo(initialDataInTargetFile)
    }

    @Test
    fun testUpload() {
        val src = sourceFile!!
        val tar = targetFile!!
        tar.writeBytes(src.readBytes())
        tar.upload()
        nhsRepository.clearAllLocalCache()
        val newTarFromRepository = nhsRepository.access(
            OnlineFile::class.java,
            targetFileDirWithName
        ).readBytes()
        assertThat(newTarFromRepository).isEqualTo(src.readBytes())
    }

}
