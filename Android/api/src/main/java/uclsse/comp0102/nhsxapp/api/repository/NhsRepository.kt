package uclsse.comp0102.nhsxapp.api.repository

import android.content.Context
import uclsse.comp0102.nhsxapp.api.R
import uclsse.comp0102.nhsxapp.api.extension.formatSubDir
import uclsse.comp0102.nhsxapp.api.extension.toURL
import uclsse.comp0102.nhsxapp.api.repository.database.BinaryData
import uclsse.comp0102.nhsxapp.api.repository.database.BinaryDataBase
import uclsse.comp0102.nhsxapp.api.repository.files.OnlineFile
import java.io.FileNotFoundException
import java.net.URL

class NhsRepository(appContext: Context) {

    private val hostAddress: URL
    private val binaryDatabase: BinaryDataBase

    init {
        hostAddress = appContext.getString(R.string.HOST_ADDRESS).toURL()
        binaryDatabase = BinaryDataBase.getInstance(appContext)
    }

    fun isAllDataClear(): Boolean {
        return binaryDatabase.dataAccessor.getAllDirtyData().isEmpty()
    }

    fun pull() {
        binaryDatabase.dataAccessor.getAllDirtyData().forEach {
            wrap(it, OnlineFile::class.java).download()
        }
    }

    fun push() {
        binaryDatabase.dataAccessor.getAll().forEach {
            wrap(it, OnlineFile::class.java).upload()
        }
    }

    fun <T : OnlineFile> access(fileType: Class<T>, fromRelativePath: String): T {
        val subDirWithName = fromRelativePath.formatSubDir()
        val data = binaryDatabase.dataAccessor.get(subDirWithName)
            ?: throw FileNotFoundException("NhsRepository.access: $fromRelativePath not found")
        return wrap(data, fileType)
    }

    fun add(vararg files: BinaryData) {
        files.forEach { binaryDatabase.dataAccessor.insert(it) }
    }

    private fun <T : OnlineFile> wrap(data: BinaryData, toType: Class<T>): T {
        val constructor = toType.getConstructor(URL::class.java, BinaryData::class.java)
        val wrappedFile = constructor.newInstance(hostAddress, data)
        wrappedFile.whenChangeOccurredOn = { changedData ->
            binaryDatabase.dataAccessor.update(changedData)
        }
        return wrappedFile
    }
}


