package com.example.assignment.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.assignment.data.database.model.PubRoom

@Dao
interface PubDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pubs: List<PubRoom>)

    @Query("SELECT * from pubs")
    fun getAllPubs(): LiveData<List<PubRoom>>

    @Query("SELECT * from pubs where id = :id")
    fun getPubById(id: Int): LiveData<List<PubRoom>>

    @Query("DELETE from pubs")
    suspend fun deleteAll()
}