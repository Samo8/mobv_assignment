package com.example.assignment.common

import androidx.room.*

@Entity(tableName = "element")
data class ElementRoom(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    @ColumnInfo
    val lat: Double,
    @ColumnInfo
    val lon: Double,
    @ColumnInfo
    val type: String,
    @Embedded
    val tags: TagsRoom,
)