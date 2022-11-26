package com.example.assignment.nnn

data class UserSignRequest(
    val name: String,
    val password: String,
)

data class PostRequestTokenRefresh(
    val refresh: String,
)