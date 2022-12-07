package com.example.assignment.data.database.model

import androidx.room.*
import com.example.assignment.common.PubData

@Entity(tableName = "pubs")
data class PubRoom(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    @ColumnInfo
    val name: String,
    @ColumnInfo
    val lat: String,
    @ColumnInfo
    val lon: String,
    @ColumnInfo
    val type: String,
    @ColumnInfo
    val users: String,
    @ColumnInfo
    val lastUpdate: String,
)