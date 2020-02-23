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

class NhsFileRepository(
    private val context: Context
) {
    private val hostAddress: URL = context.getString(R.string.HOST_ADDRESS).toURL()
    private val binaryDatabase: BinaryDataBase = BinaryDataBase.getInstance(context)

    fun <T : OnlineFile> access(fileType: Class<T>, fromRelativePath: String): T {
        val subDirWithName = fromRelativePath.formatSubDir()
        val data = binaryDatabase.dataAccessor.get(subDirWithName)
            ?: throw FileNotFoundException("NhsRepository.access: $fromRelativePath not found")
        val newOnlineFile = fileType.getConstructor(
            URL::class.java,
            Context::class.java,
            BinaryData::class.java
        ).newInstance(hostAddress, context, data)
        if (newOnlineFile.isEmpty()) {
            newOnlineFile.update()
            newOnlineFile.flush()
        }
        return newOnlineFile
    }
}


