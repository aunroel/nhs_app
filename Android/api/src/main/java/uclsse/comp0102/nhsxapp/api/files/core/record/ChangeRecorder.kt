package uclsse.comp0102.nhsxapp.api.files.core.record

import android.content.Context
import uclsse.comp0102.nhsxapp.api.files.core.record.database.Record
import uclsse.comp0102.nhsxapp.api.files.core.record.database.RecordDatabase

class ChangeRecorder(appContext: Context, identifier: String) {
    companion object {
        private var flyWeightDatabase: RecordDatabase? = null

        @Synchronized
        private fun getDatabase(appContext: Context): RecordDatabase {
            flyWeightDatabase = flyWeightDatabase
                ?: RecordDatabase.getInstance(appContext)
            return flyWeightDatabase!!
        }
    }


    private val database: RecordDatabase
    private val record: Record

    init {
        database = getDatabase(appContext)
        var tmpData = database.dataAccessor.get(identifier)
        if (tmpData == null) {
            tmpData = Record(identifier)
            database.dataAccessor.insert(tmpData)
        }
        record = tmpData
    }

    var lastModifiedTime: Long
        get() {
            return record.lastModifiedTime
        }
        set(value) {
            record.lastModifiedTime = value
            database.dataAccessor.update(record)
        }

    var lastUpdateTime: Long
        get() {
            return record.lastUpdateTime
        }
        set(value) {
            record.lastUpdateTime = value
            database.dataAccessor.update(record)
        }

    var lastUploadTime: Long
        get() {
            return record.lastUploadTime
        }
        set(value) {
            record.lastUploadTime = value
            database.dataAccessor.update(record)
        }


}