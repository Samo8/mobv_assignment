package com.example.assignment.common

import com.google.gson.annotations.SerializedName

class PubData(
    @SerializedName("documents")
    val elements: MutableList<Element>,
)