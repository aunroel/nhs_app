package uclsse.comp0102.nhsxapp.api.files.core.record.database

import androidx.room.*

@Dao
interface RecordsDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(vararg file: Record)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(vararg file: Record)

    @Query("SELECT * from file_change_records_table")
    fun getAll(): List<Record>

    @Query("SELECT * from file_change_records_table WHERE last_upload_time < last_modified_time")
    fun getAllDirtyData(): List<Record>

    @Query("SELECT * from file_change_records_table WHERE id_string = :idStr")
    fun get(idStr: String): Record?

}