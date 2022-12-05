package com.example.assignment.data.api.model

import kotlinx.serialization.SerialName

class PubDetail(
    @SerialName("elements")
    val elements: List<Element>
)