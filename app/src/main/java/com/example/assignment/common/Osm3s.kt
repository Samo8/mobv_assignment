package com.example.assignment.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Osm3s(
    val copyright: String,
    @SerialName("timestamp_osm_base")
    val timestampOsmBase: String
)