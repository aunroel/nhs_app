package uclsse.comp0102.nhsxapp.api.files.local.database

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
    var lastUpdateTime: Long = 0,
    @ColumnInfo(name = "file_content", typeAffinity = ColumnInfo.BLOB)
    var data: ByteArray = ByteArray(0)

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Record

        if (identifierStr != other.identifierStr) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = identifierStr.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }
}