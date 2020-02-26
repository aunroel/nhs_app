package uclsse.comp0102.nhsxapp.api.files.core.record.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Record::class], version = 1, exportSchema = false)
abstract class RecordDatabase : RoomDatabase() {

    abstract val dataAccessor: RecordsDao

    companion object {
        @Volatile
        private var INSTANCE: RecordDatabase? = null
        private const val DATABASE_NAME = "online_file_database"

        @Synchronized
        fun getInstance(context: Context): RecordDatabase {
            if (INSTANCE != null) return INSTANCE!!
            val builder = Room.databaseBuilder(
                context.applicationContext,
                RecordDatabase::class.java,
                DATABASE_NAME
            )
            INSTANCE = builder
                .fallbackToDestructiveMigration()
                .build()
            return INSTANCE!!
        }
    }
}