package com.example.assignment

import android.app.Application

class PubsApplication: Application() {
    val database: PubsRoomDatabase by lazy { PubsRoomDatabase.getDatabase(this) }
}