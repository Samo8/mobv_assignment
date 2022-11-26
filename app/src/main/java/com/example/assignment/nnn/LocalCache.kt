package com.example.assignment.nnn

import androidx.lifecycle.LiveData
import com.example.assignment.room.dao.PubDao
import com.example.assignment.room.model.PubRoom

class LocalCache(private val dao: PubDao) {
    suspend fun addPubs(bars: List<PubRoom>){
        dao.insertAll(bars)
    }

    suspend fun deletePubs(){ dao.deleteAll() }

    fun getPubs(): LiveData<List<PubRoom>> = dao.getAllPubs()
}