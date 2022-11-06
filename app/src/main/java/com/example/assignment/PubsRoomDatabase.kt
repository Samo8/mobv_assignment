package com.example.assignment

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.assignment.common.ElementRoom
import com.example.assignment.dao.ElementDao

@Database(entities = [ElementRoom::class], version = 1, exportSchema = false)
abstract class PubsRoomDatabase: RoomDatabase() {
    abstract fun elementDao(): ElementDao

    companion object {
        @Volatile
        private var INSTANCE: PubsRoomDatabase? = null
        fun getDatabase(context: Context): PubsRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PubsRoomDatabase::class.java,
                    "item_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}