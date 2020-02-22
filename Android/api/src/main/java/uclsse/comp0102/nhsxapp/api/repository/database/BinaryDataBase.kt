package uclsse.comp0102.nhsxapp.api.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BinaryData::class], version = 1, exportSchema = false)
abstract class BinaryDataBase : RoomDatabase() {

    abstract val dataAccessor: BinaryDataDao

    companion object {
        @Volatile
        private var INSTANCE: BinaryDataBase? = null
        private const val DATABASE_NAME = "online_file_database"

        @Synchronized
        fun getInstance(context: Context): BinaryDataBase {
            if (INSTANCE != null) return INSTANCE!!
            val builder = Room.databaseBuilder(
                context.applicationContext,
                BinaryDataBase::class.java,
                DATABASE_NAME
            )
            INSTANCE = builder
                .fallbackToDestructiveMigration()
                .build()
            return INSTANCE!!
        }
    }
}