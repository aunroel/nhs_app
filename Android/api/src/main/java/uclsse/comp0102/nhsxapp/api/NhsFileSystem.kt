package uclsse.comp0102.nhsxapp.api

import android.content.Context
import uclsse.comp0102.nhsxapp.api.extension.formatSubDir
import uclsse.comp0102.nhsxapp.api.extension.toURL
import uclsse.comp0102.nhsxapp.api.files.AbsOnlineFile
import uclsse.comp0102.nhsxapp.api.files.local.LocalRecord
import uclsse.comp0102.nhsxapp.api.files.local.database.RecordDatabase
import java.net.URL

class NhsFileSystem(
    private val appContext: Context
) {

    private val cache: MutableMap<String, LocalRecord> = mutableMapOf()
    private val hostAddress: URL = appContext.getString(R.string.HOST_ADDRESS).toURL()

    fun <T : AbsOnlineFile> access(fileType: Class<T>, fromRelativePath: String): T {
        return fileType.getConstructor(
            URL::class.java,
            String::class.java,
            Context::class.java
        ).newInstance(hostAddress, fromRelativePath.formatSubDir(), appContext)
    }

    fun clearAllLocalCache() {
        RecordDatabase.getInstance(appContext).clearAllTables()
    }
}