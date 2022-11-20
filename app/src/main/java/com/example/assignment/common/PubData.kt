package com.example.assignment.common

import kotlinx.serialization.SerialName

class PubData(
    @SerialName("bar_id")
    val bar_id: String,
    @SerialName("bar_name")
    val bar_name: String,
    val lat: String,
    val lon: String,
    @SerialName("bar_type")
    val bar_type: String,
    val users: String,
    @SerialName("last_update")
    val last_update: String,
)