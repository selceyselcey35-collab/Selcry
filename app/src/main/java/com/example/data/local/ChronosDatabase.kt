package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        FavoriteWatchEntity::class,
        ComparisonWatchEntity::class,
        DropshipEntity::class,
        ReturnClaimEntity::class,
        ReviewEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class ChronosDatabase : RoomDatabase() {
    abstract fun watchDao(): WatchDao

    companion object {
        @Volatile
        private var INSTANCE: ChronosDatabase? = null

        fun getDatabase(context: Context): ChronosDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChronosDatabase::class.java,
                    "chronos_luxury_vault.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
