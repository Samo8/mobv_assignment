package com.example.assignment.room.model

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
) {
    fun toPubData(): PubData {
        return PubData(
            bar_id = id,
            bar_name = name,
            lat = lat,
            lon = lon,
            bar_type = type,
            users = users,
            last_update = lastUpdate,
        )
    }
}