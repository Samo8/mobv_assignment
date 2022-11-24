package com.example.assignment

import com.example.assignment.pub_detail.model.Element
import kotlinx.serialization.Serializable

@Serializable
data class PubsAroundResponse(
    val elements: List<Element>
)
