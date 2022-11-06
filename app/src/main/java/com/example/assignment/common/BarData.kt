package com.example.assignment.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BarData(
//    @SerialName("documents")
    val elements: MutableList<Element>,
    val generator: String,
    val osm3s: Osm3s,
    val version: Double
)