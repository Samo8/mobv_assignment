package com.example.assignment.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.assignment.common.ElementRoom

@Dao
interface ElementDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(element: ElementRoom)

    @Update
    suspend fun update(element: ElementRoom)

    @Delete
    suspend fun delete(element: ElementRoom)

    @Query("SELECT * from element")
    suspend fun getAllPubs(): List<ElementRoom>

    @Query("SELECT * from element where id = :id")
    fun getPubById(id: Int): LiveData<List<ElementRoom>>
}