package uclsse.comp0102.nhsxapp.api.repository.files

import android.content.Context
import uclsse.comp0102.nhsxapp.api.repository.database.BinaryData
import uclsse.comp0102.nhsxapp.api.repository.files.wrapper.LocalAccessible
import uclsse.comp0102.nhsxapp.api.repository.files.wrapper.LocalDataWrapper
import uclsse.comp0102.nhsxapp.api.repository.files.wrapper.OnlineAccessible
import uclsse.comp0102.nhsxapp.api.repository.files.wrapper.OnlineDataWrapper
import java.net.URL

open class OnlineFile(onHost: URL, appContext: Context, core: BinaryData) :
    OnlineAccessible by OnlineDataWrapper(onHost, core),
    LocalAccessible by LocalDataWrapper(appContext, core) {

    private val binaryData: BinaryData = core

    fun isDirty(): Boolean {
        return binaryData.lastModifiedTime > binaryData.lastUploadTime
    }

    fun isEmpty(): Boolean {
        return readBytes().isEmpty()
    }
}