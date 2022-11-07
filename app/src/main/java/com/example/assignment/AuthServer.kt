package com.example.assignment

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AuthServer {
    private const val URL = "https://zadanie.mpage.sk"
    private val authService: AuthService

    init {
        val client = OkHttpClient.Builder().build()
        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        authService = retrofit.create(AuthService::class.java)
    }

    suspend fun register (name: String, password: String): String = withContext(Dispatchers.IO) {
        val request = AuthService.PostRequest(
            api_key = "KHUu1Fo8042UwzczKz9nNeuVOsg2T4ClIfhndD2Su0G0LHHCBf0LnUF05L231J0M",
            name = name,
            password = password,
        )

        val response = authService.register(request)
        if (response.isSuccessful) {
            val body = response.body()!!
            return@withContext body
        } else {
            throw Exception(response.errorBody()?.charStream()?.readText())
        }
    }

    suspend fun login (name: String, password: String): String = withContext(Dispatchers.IO) {
        val request = AuthService.PostRequest(
            api_key = "KHUu1Fo8042UwzczKz9nNeuVOsg2T4ClIfhndD2Su0G0LHHCBf0LnUF05L231J0M",
            name = name,
            password = password,
        )

        val response = authService.login(request)
        if (response.isSuccessful) {
            val body = response.body()!!
            return@withContext body
        } else {
            throw Exception(response.errorBody()?.charStream()?.readText())
        }
    }

    suspend fun refresh (uid: String, refreshToken: String): String = withContext(Dispatchers.IO) {
        val request = AuthService.PostRequestTokenRefresh(
            api_key = "KHUu1Fo8042UwzczKz9nNeuVOsg2T4ClIfhndD2Su0G0LHHCBf0LnUF05L231J0M",
            uid = uid,
            refresh = refreshToken ,
        )

        val response = authService.refreshToken(request)
        if (response.isSuccessful) {
            val body = response.body()!!
            return@withContext body
        } else {
            throw Exception(response.errorBody()?.charStream()?.readText())
        }
    }
}