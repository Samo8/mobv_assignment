package com.example.assignment.room.model

import androidx.room.*
import com.example.assignment.common.Element

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
) {
    fun toElement(): Element {
        return Element(
            id = id,
            lat = lat,
            lon = lon,
            tags = tags.toTags(),
            type = type,
        )
    }
}