package com.example.assignment.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.assignment.room.model.PubRoom
import kotlinx.coroutines.flow.Flow

@Dao
interface PubDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(pub: PubRoom)

    @Update
    suspend fun update(pub: PubRoom)

    @Delete
    suspend fun delete(pub: PubRoom)

    @Query("SELECT * from pubs")
    fun getAllPubs(): Flow<List<PubRoom>>

    @Query("SELECT * from pubs where id = :id")
    fun getPubById(id: Int): LiveData<List<PubRoom>>
}