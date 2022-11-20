package com.example.assignment.pub_detail.model

import kotlinx.serialization.Serializable

@Serializable
data class Element(
    val id: Long,
    val lat: Double,
    val lon: Double,
    val tags: Tags,
    val type: String
)