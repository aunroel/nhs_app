package uclsse.comp0102.nhsxapp.api.files

import android.content.Context
import uclsse.comp0102.nhsxapp.api.R
import uclsse.comp0102.nhsxapp.api.extension.formatSubDir
import uclsse.comp0102.nhsxapp.api.extension.toURL
import uclsse.comp0102.nhsxapp.api.files.core.OnlineFile
import uclsse.comp0102.nhsxapp.api.files.core.record.database.RecordDatabase
import java.io.File
import java.net.URL

class NhsFileSystem(
    private val appContext: Context
) {

    private val hostAddress: URL = appContext.getString(R.string.HOST_ADDRESS).toURL()

    fun <T : OnlineFile> access(fileType: Class<T>, fromRelativePath: String): T {
        return fileType.getConstructor(
            URL::class.java,
            String::class.java,
            Context::class.java
        ).newInstance(hostAddress, fromRelativePath.formatSubDir(), appContext)
    }

    fun clearAllLocalCache() {
        val database = RecordDatabase.getInstance(appContext)
        database.dataAccessor.getAll().forEach { record ->
            val subDirWithFileName = record.identifierStr
            try {
                File(appContext.filesDir, subDirWithFileName).delete()
            } catch (ignore: Exception) {
            }
        }
        database.clearAllTables()

    }
}