package com.example.assignment.user

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface UserService {
    data class UserPostRequest(
        val contact: String,
    )

    @POST("/contact/message.php")
    suspend fun addFriend(
        @HeaderMap headers: Map<String, String>,
        @Body request: UserPostRequest,
    ): Response<Void>

    @GET("/contact/list.php")
    suspend fun fetchFriends(@HeaderMap headers: Map<String, String>): Response<List<Friend>>
}