package uclsse.comp0102.nhsxapp.api.repository

import uclsse.comp0102.nhsxapp.api.extension.formatUriSubDir
import uclsse.comp0102.nhsxapp.api.extension.merge
import uclsse.comp0102.nhsxapp.api.repository.files.GlobalFile
import java.io.File
import java.io.FileNotFoundException
import java.net.URI

class NhsRepository private constructor(
    private val onlineUri: URI,
    private val localUri: URI
) {
    companion object {
        private var _instance: NhsRepository? = null
        var instance: NhsRepository?
            get() = _instance
            private set(value) {
                _instance = value
            }

        fun setUri(onlineUri: URI, localUri: URI): Boolean {
            if (instance != null) return false
            instance = NhsRepository(onlineUri, localUri)
            return true
        }
    }

    private val storedFilesList: MutableList<GlobalFile> = mutableListOf()

    fun isAllDataClear() = storedFilesList.none { it.isDirty() }

    fun pull() = storedFilesList.forEach { it.downloadOnlineVersion() }

    fun push() = storedFilesList.forEach { if (it.isDirty()) it.uploadLocalVersion() }

    fun access(fileName: String, fromSubPath: String = ""): GlobalFile {
        val relativePath = "$fromSubPath/${fileName}".formatUriSubDir()
        val repositoryDir = File(localUri)
        val result = storedFilesList.find { it.toRelativeString(repositoryDir) == relativePath }
        return result
            ?: throw FileNotFoundException("NhsRepository.access: $relativePath not found ")
    }


    fun <T : GlobalFile> add(inputFile: T): Boolean {
        if (storedFilesList.contains(inputFile))
            return false
        storedFilesList.add(inputFile)
        return true
    }

    inner class Builder {
        private val REPOSITORY_BACKUP_FILENAME = ".repository.backup"

        private var onlineUri = URI("http://localhost/")
        private var localUri = URI("file://home/")

        fun setOnlineUri(uri: URI): Builder {
            onlineUri = uri
            return this
        }

        fun setLocalUri(uri: URI): Builder {
            localUri = uri
            return this
        }

        fun build(): NhsRepository {
            val tmpBackupFile =
        }
    }


    inner class FileBuilder {
        private var fileName = "tmp.file"
        private var fileSubDir = "tmp/file"
        private var isOverwritePreviousData = false
        private var fileContent: ByteArray? = null

        fun setFileName(excepted: String): FileBuilder {
            fileName = excepted
            return this
        }

        fun setFileSubDir(excepted: String): FileBuilder {
            fileSubDir = excepted
            return this
        }

        fun setFileContent(excepted: ByteArray, isOverwrite: Boolean = false): FileBuilder {
            fileContent = excepted
            isOverwritePreviousData = isOverwrite
            return this
        }

        fun <T : GlobalFile> build(expected: Class<T>): T {
            val onlineFilePathUri = onlineUri.merge(fileSubDir)
            val localFilePathUri = localUri.merge(fileSubDir)
            val newFile = expected.getConstructor(
                URI::class.java,
                URI::class.java,
                String::class.java
            ).newInstance(onlineFilePathUri, localFilePathUri, fileName)
            val parentPath = File(localFilePathUri)
            if (!parentPath.exists()) parentPath.mkdirs()
            if (!newFile.exists()) newFile.createNewFile()
            if (isEssentialToWriteContentInto(newFile)) newFile.writeBytes(fileContent!!)
            return newFile
        }

        private fun isEssentialToWriteContentInto(file: File): Boolean =
            fileContent != null && (isOverwritePreviousData || file.readBytes().isEmpty())
    }
}


