package com.example.assignment.nnn

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.assignment.room.PubsRoomDatabase

object Injection {
    private fun provideCache(context: Context): LocalCache {
        val database = PubsRoomDatabase.getInstance(context)
        return LocalCache(database.pubDao())
    }

    fun provideDataRepository(context: Context): DataRepository {
        return DataRepository.getInstance(RestApi.create(context), provideCache(context))
    }

    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(
            provideDataRepository(
                context
            )
        )
    }
}