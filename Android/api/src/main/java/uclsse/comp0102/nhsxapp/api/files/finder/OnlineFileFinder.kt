package uclsse.comp0102.nhsxapp.api.files.finder

import android.content.Context
import uclsse.comp0102.nhsxapp.api.extension.formatSubDir
import uclsse.comp0102.nhsxapp.api.files.AbsOnlineFile
import uclsse.comp0102.nhsxapp.api.files.local.database.RecordDatabase
import java.net.URL

/** Factory class for creating different types of online files
 */
class OnlineFileFinder(
    private val onHost: URL,
    private val appContext: Context
) {

    /** get access to the online files
     */
    fun <T : AbsOnlineFile> access(fileType: Class<T>, fromRelativePath: String): T {
        return fileType.getConstructor(
            URL::class.java,
            String::class.java,
            Context::class.java
        ).newInstance(onHost, fromRelativePath.formatSubDir(), appContext)
    }

    /** Clear cache in the local database
     */
    fun clearAllLocalCache() {
        RecordDatabase.getInstance(appContext).clearAllTables()
    }
}