package uclsse.comp0102.nhsxapp.api.files.finder

import android.content.Context
import uclsse.comp0102.nhsxapp.api.extension.formatSubDir
import uclsse.comp0102.nhsxapp.api.files.AbsOnlineFile
import uclsse.comp0102.nhsxapp.api.files.local.database.RecordDatabase
import java.net.URL

class OnlineFileFinder(
    private val onHost: URL,
    private val appContext: Context
) {

    fun <T : AbsOnlineFile> access(fileType: Class<T>, fromRelativePath: String): T {
        return fileType.getConstructor(
            URL::class.java,
            String::class.java,
            Context::class.java
        ).newInstance(onHost, fromRelativePath.formatSubDir(), appContext)
    }

    fun clearAllLocalCache() {
        RecordDatabase.getInstance(appContext).clearAllTables()
    }
}