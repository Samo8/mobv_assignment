package com.example.assignment.server

import com.example.assignment.auth.AuthService
import com.example.assignment.data.models.AuthData
import com.example.assignment.user.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


object MpageServer {
    private const val URL = "https://zadanie.mpage.sk"
    private val authService: AuthService
    private val userService: UserService

    init {
        val client = OkHttpClient.Builder().build()
        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        authService = retrofit.create(AuthService::class.java)
        userService = retrofit.create(UserService::class.java)
    }

    suspend fun register (name: String, password: String): AuthData = withContext(Dispatchers.IO) {
        val request = AuthService.PostRequest(
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

    suspend fun login (name: String, password: String): AuthData = withContext(Dispatchers.IO) {
        val request = AuthService.PostRequest(
            name = name,
            password = password,
        )

        val response = authService.login(request)
        if (response.isSuccessful) {
            val loginResponse = response.body()!!
            if (loginResponse.uid == "-1") {
                throw Exception("Nesprávne prihlasovacie údaje")
            }
            return@withContext loginResponse
        } else {
            throw Exception(response.errorBody()?.charStream()?.readText())
        }
    }

    suspend fun refresh (uid: String, refreshToken: String): String = withContext(Dispatchers.IO) {
        val request = AuthService.PostRequestTokenRefresh(
            uid = uid,
            refresh = refreshToken,
        )

        val response = authService.refreshToken(request)
        if (response.isSuccessful) {
            val body = response.body()!!
            return@withContext body
        } else {
            throw Exception(response.errorBody()?.charStream()?.readText())
        }
    }

    suspend fun addFriend (authData: AuthData, friendName: String) = withContext(Dispatchers.IO) {
        val request = UserService.UserPostRequest(friendName)

        val response = userService.addFriend(
            headers = mapOf(
                "authorization" to "Bearer ${authData.access}",
                "x-apikey" to "c95332ee022df8c953ce470261efc695ecf3e784",
                "x-user" to authData.uid,
            ),
            request = request,
        )
        if (!response.isSuccessful) {
            println(response.errorBody())
            println(response.errorBody()?.charStream()?.readText())
            throw Exception(response.errorBody()?.charStream()?.readText())
        }
    }
}