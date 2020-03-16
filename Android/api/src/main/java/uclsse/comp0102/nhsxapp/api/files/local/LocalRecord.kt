package uclsse.comp0102.nhsxapp.api.files.local

import android.content.Context
import uclsse.comp0102.nhsxapp.api.files.local.database.Record
import uclsse.comp0102.nhsxapp.api.files.local.database.RecordDatabase

// It is a encapsulation of the room database
// and also an re-implementation of local file
// It is used to store the binary data of a file
// with its last modified time, last update and
// upload time.
open class LocalRecord(idStr: String, appContext: Context) {

    // These static field and method are used
    // to implement the flyweight design pattern
    // for reducing the memory space.
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
    private var record: Record

    init {
        database = getDatabase(appContext)
        var tmpData = database.dataAccessor.get(idStr)
        if (tmpData == null) {
            tmpData = Record(idStr)
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

    var content: ByteArray
        get(){
            return record.data
        }
        set(value){
            record.data = value
            database.dataAccessor.update(record)
        }

    fun refreshCache(){
        val idStr = record.identifierStr
        record = database.dataAccessor.get(idStr)!!
    }




}