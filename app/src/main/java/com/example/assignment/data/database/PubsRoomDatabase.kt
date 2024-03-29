package com.example.assignment.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.assignment.data.database.dao.PubDao
import com.example.assignment.data.database.model.PubRoom

@Database(entities = [PubRoom::class], version = 1, exportSchema = false)
abstract class PubsRoomDatabase: RoomDatabase() {
    abstract fun pubDao(): PubDao

    companion object {
        @Volatile
        private var INSTANCE: PubsRoomDatabase? = null

        fun getInstance(context: Context): PubsRoomDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also {  INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                PubsRoomDatabase::class.java, "barsDatabase"
            ).fallbackToDestructiveMigration()
                .build()
//        fun getDatabase(context: Context): PubsRoomDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    PubsRoomDatabase::class.java,
//                    "item_database"
//                )
//                    .fallbackToDestructiveMigration()
//                    .build()
//                INSTANCE = instance
//                return instance
//            }
//        }
    }
}