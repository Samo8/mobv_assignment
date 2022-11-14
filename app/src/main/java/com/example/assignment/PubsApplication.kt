package com.example.assignment

import android.app.Application
import com.example.assignment.room.PubsRoomDatabase

class PubsApplication: Application() {
    val database: PubsRoomDatabase by lazy { PubsRoomDatabase.getDatabase(this) }
}