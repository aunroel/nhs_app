package uclsse.comp0102.nhsxapp.api.repository

import android.content.Context
import uclsse.comp0102.nhsxapp.api.R
import uclsse.comp0102.nhsxapp.api.extension.formatSubDir
import uclsse.comp0102.nhsxapp.api.extension.toURL
import uclsse.comp0102.nhsxapp.api.repository.database.BinaryData
import uclsse.comp0102.nhsxapp.api.repository.files.OnlineFile
import java.net.URL

class NhsFileRepository(
    private val context: Context
) {
    private val hostAddress: URL = context.getString(R.string.HOST_ADDRESS).toURL()

    fun <T : OnlineFile> access(fileType: Class<T>, fromRelativePath: String): T {
        return fileType.getConstructor(
            URL::class.java,
            Context::class.java,
            BinaryData::class.java
        ).newInstance(hostAddress, fromRelativePath.formatSubDir(), context)
    }

}


