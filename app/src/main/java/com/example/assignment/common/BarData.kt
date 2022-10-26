package com.example.assignment.common

import kotlinx.serialization.Serializable

@Serializable
data class BarData(
    val elements: MutableList<Element>,
    val generator: String,
    val osm3s: Osm3s,
    val version: Double
)