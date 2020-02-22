package uclsse.comp0102.nhsxapp.api.repository.database

import androidx.room.*

@Dao
interface BinaryDataDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(vararg file: BinaryData)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(vararg file: BinaryData)

    @Query("SELECT * from online_file_table")
    fun getAll(): List<BinaryData>

    @Query("SELECT * from online_file_table WHERE last_upload_time < last_modified_time")
    fun getAllDirtyData(): List<BinaryData>

    @Query("SELECT * from online_file_table WHERE sub_dir_with_file_name = :subDirWithName")
    fun get(subDirWithName: String): BinaryData?

}