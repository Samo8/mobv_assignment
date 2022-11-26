package com.example.assignment.nnn

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