package uclsse.comp0102.nhsxapp.api.repository.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "online_file_table")
data class BinaryData(
    @PrimaryKey
    @ColumnInfo(name = "sub_dir_with_file_name")
    val subDirWithFileName: String,
    @ColumnInfo(name = "last_modified_time")
    var lastModifiedTime: Long = 0,
    @ColumnInfo(name = "last_upload_time")
    var lastUploadTime: Long = 0,
    @ColumnInfo(name = "file_contents", typeAffinity = ColumnInfo.BLOB)
    var data: ByteArray = ByteArray(0)
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as BinaryData
        if (subDirWithFileName != other.subDirWithFileName) return false
        return true
    }

    override fun hashCode(): Int {
        return subDirWithFileName.hashCode()
    }
}