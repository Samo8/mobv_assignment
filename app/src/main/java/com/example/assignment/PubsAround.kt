package com.example.assignment

import com.example.assignment.pub_detail.model.Element
import kotlinx.serialization.Serializable

@Serializable
data class PubsAround(
    val elements: List<Element>
)
