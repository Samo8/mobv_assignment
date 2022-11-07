package com.example.assignment

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    data class PostRequest(
        val api_key: String,
        val name: String,
        val password: String,
    )

    data class PostRequestTokenRefresh(
        val api_key: String,
        val uid: String,
        val refresh: String,
    )

//    @Headers("api-key: KHUu1Fo8042UwzczKz9nNeuVOsg2T4ClIfhndD2Su0G0LHHCBf0LnUF05L231J0M")
    @POST("/user/create.php")
    suspend fun register(@Body request: PostRequest): Response<String>

    @POST("/user/login.php")
    suspend fun login(@Body request: PostRequest): Response<String>

    @POST("/user/refresh.php")
    suspend fun refreshToken(@Body request: PostRequestTokenRefresh): Response<String>
}