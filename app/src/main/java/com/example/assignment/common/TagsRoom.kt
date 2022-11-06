package com.example.assignment.common

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName

@Entity(tableName = "tag")
data class TagsRoom(
    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = "tagsId")
    val id: Int = 0,
    @ColumnInfo
    val bar: String?,
    @ColumnInfo
    val email: String?,
    @ColumnInfo
    val name: String?,
    @ColumnInfo
    val url: String?,
)