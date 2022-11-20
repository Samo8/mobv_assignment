package com.example.assignment.pub_detail.model

import com.example.assignment.pub_detail.model.Element
import kotlinx.serialization.SerialName

class PubDetail(
    @SerialName("elements")
    val elements: List<Element>
)