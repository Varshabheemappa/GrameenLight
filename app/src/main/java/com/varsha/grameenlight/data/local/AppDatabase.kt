package com.varsha.grameenlight.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PoleEntity::class, ReportEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun poleDao(): PoleDao
    abstract fun reportDao(): ReportDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "grameen_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}