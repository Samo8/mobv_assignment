package com.example.assignment.auth

import com.example.assignment.data.models.AuthData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthService {
    data class PostRequest(
        val name: String,
        val password: String,
    )

    data class PostRequestTokenRefresh(
        val refresh: String,
    )

    @Headers("x-apikey: c95332ee022df8c953ce470261efc695ecf3e784")
    @POST("/user/create.php")
    suspend fun register(@Body request: PostRequest): Response<AuthData>

    @Headers("x-apikey: c95332ee022df8c953ce470261efc695ecf3e784")
    @POST("/user/login.php")
    suspend fun login(@Body request: PostRequest): Response<AuthData>

    @POST("/user/refresh.php")
    suspend fun refreshToken(
        @HeaderMap headers: Map<String, String>,
        @Body request: PostRequestTokenRefresh,
    ): Response<AuthData>
}