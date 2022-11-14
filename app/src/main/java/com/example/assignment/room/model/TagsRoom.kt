package com.example.assignment.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.assignment.common.Tags
import kotlinx.serialization.SerialName

@Entity(tableName = "tag")
data class TagsRoom(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tagsId")
    val id: Int = 0,
    @ColumnInfo
    val bar: String?,
    @ColumnInfo
    val email: String?,
    @ColumnInfo
    val name: String?,
    @ColumnInfo
    val url: String?,
) {
    fun toTags(): Tags {
        return Tags(
            bar = bar,
            email = email,
            name = name,
            url = url,
        )
    }
}