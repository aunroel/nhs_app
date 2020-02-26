package uclsse.comp0102.nhsxapp.api.files.core.record.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "file_change_records_table")

data class Record(
    @PrimaryKey
    @ColumnInfo(name = "id_string")
    val identifierStr: String,
    @ColumnInfo(name = "last_modified_time")
    var lastModifiedTime: Long = 0,
    @ColumnInfo(name = "last_upload_time")
    var lastUploadTime: Long = 0,
    @ColumnInfo(name = "last_update_time")
    var lastUpdateTime: Long = 0
)