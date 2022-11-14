package com.example.assignment.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.assignment.room.model.ElementRoom
import kotlinx.coroutines.flow.Flow

@Dao
interface ElementDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(element: ElementRoom)

    @Update
    suspend fun update(element: ElementRoom)

    @Delete
    suspend fun delete(element: ElementRoom)

    @Query("SELECT * from element")
    fun getAllPubs(): Flow<List<ElementRoom>>

    @Query("SELECT * from element where id = :id")
    fun getPubById(id: Int): LiveData<List<ElementRoom>>
}