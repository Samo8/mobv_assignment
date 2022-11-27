package com.example.assignment

import android.app.Application
import com.example.assignment.data.database.PubsRoomDatabase

class PubsApplication: Application() {
    val database: PubsRoomDatabase by lazy { PubsRoomDatabase.getInstance(this) }
}