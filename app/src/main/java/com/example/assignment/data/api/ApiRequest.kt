package com.example.assignment.data.api

import com.example.assignment.pub_detail.model.Element

data class UserSignRequest(
    val name: String,
    val password: String,
)

data class RefreshTokenRequest(
    val refresh: String,
)

data class AddFriendRequest(
    val contact: String,
)

data class Friend(
    val user_id: String,
    val user_name: String,
    val bar_id: String?,
    val bar_name: String?,
    val time: String?,
    val bar_lat: String?,
    val bar_lon: String?
)

data class PubsAroundResponse(
    val elements: List<Element>
)