package uclsse.comp0102.nhsxapp.api.repository.files.wrapper

import android.content.Context
import uclsse.comp0102.nhsxapp.api.repository.database.BinaryData
import uclsse.comp0102.nhsxapp.api.repository.database.BinaryDataBase

class LocalDataWrapper(appContext: Context, core: BinaryData) : LocalAccessible {

    companion object {
        private var flyWeightDatabase: BinaryDataBase? = null
        @Synchronized
        private fun getDatabase(appContext: Context): BinaryDataBase {
            flyWeightDatabase = flyWeightDatabase
                ?: BinaryDataBase.getInstance(appContext)
            return flyWeightDatabase!!
        }
    }

    private val binaryData: BinaryData = core
    private val database: BinaryDataBase = getDatabase(appContext)

    override fun readBytes(): ByteArray {
        return binaryData.data
    }

    override fun writeBytes(bytes: ByteArray) {
        binaryData.data = bytes
        binaryData.lastModifiedTime = System.currentTimeMillis()
        database.dataAccessor.update(binaryData)
    }

    override fun flush() {
        database.dataAccessor.update(binaryData)
    }
}