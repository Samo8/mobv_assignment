package com.example.assignment.server

import com.example.assignment.PubsService
import com.example.assignment.SessionManager
import com.example.assignment.auth.AuthServer.refresh
import com.example.assignment.auth.AuthService
import com.example.assignment.common.PubData
import com.example.assignment.auth.AuthData
import com.example.assignment.user.Friend
import com.example.assignment.user.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object MpageServer {
    private const val URL = "https://zadanie.mpage.sk"
    private val authService: AuthService
    private val userService: UserService
    private val pubsService: PubsService

    init {
        val client = OkHttpClient
            .Builder()
//            .authenticator(TokenAuthenticator())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        authService = retrofit.create(AuthService::class.java)
        userService = retrofit.create(UserService::class.java)
        pubsService = retrofit.create(PubsService::class.java)
    }

    suspend fun fetchBarList (
        authData: AuthData,
        sessionManager: SessionManager,
    ): List<PubData> = withContext(Dispatchers.IO) {
        val response = pubsService.fetchBarList(
            headers = mapOf(
                "authorization" to "Bearer ${authData.access}",
                "x-apikey" to "c95332ee022df8c953ce470261efc695ecf3e784",
                "x-user" to authData.uid,
            ),
        )
        if (response.code() == 401) {
            try {
                val updatedAuthData = refresh(authData.uid, authData.refresh, sessionManager)
                fetchBarList(updatedAuthData, sessionManager)
            } catch (e: Exception) {
                throw Exception(e.toString())
            }
        } else {
            if (response.isSuccessful) {
                return@withContext response.body() ?: mutableListOf()
            } else {
                println(response.errorBody())
                println(response.errorBody()?.charStream()?.readText())
                throw Exception(response.errorBody()?.charStream()?.readText())
            }
        }
    }

    suspend fun addFriend (
        authData: AuthData,
        friendName: String,
        sessionManager: SessionManager,
    ): Unit = withContext(Dispatchers.IO) {
        val request = UserService.UserPostRequest(friendName)

        val response = userService.addFriend(
            headers = mapOf(
                "authorization" to "Bearer ${authData.access}",
                "x-apikey" to "c95332ee022df8c953ce470261efc695ecf3e784",
                "x-user" to authData.uid,
            ),
            request = request,
        )
        if (response.code() == 401) {
         try {
             val updatedAuthData = refresh(authData.uid, authData.refresh, sessionManager)
             addFriend(updatedAuthData, friendName, sessionManager)
         } catch (e: Exception) {
             throw Exception(e.toString())
         }
        } else {
            if (!response.isSuccessful) {
                println(response.errorBody())
                println(response.errorBody()?.charStream()?.readText())
                throw Exception(response.errorBody()?.charStream()?.readText())
            }
        }
    }

    suspend fun fetchFriends (
        authData: AuthData,
        sessionManager: SessionManager,
    ): List<Friend> = withContext(Dispatchers.IO) {
        val response = userService.fetchFriends(
            headers = mapOf(
                "authorization" to "Bearer ${authData.access}",
                "x-apikey" to "c95332ee022df8c953ce470261efc695ecf3e784",
                "x-user" to authData.uid,
            ),
        )
        if (response.code() == 401) {
            try {
                val updatedAuthData = refresh(authData.uid, authData.refresh, sessionManager)
                fetchFriends(updatedAuthData, sessionManager)
            } catch (e: Exception) {
                throw Exception(e.toString())
            }
        } else {
            if (response.isSuccessful) {
                return@withContext response.body() ?: mutableListOf()
            } else {
                println(response.errorBody())
                println(response.errorBody()?.charStream()?.readText())
                throw Exception(response.errorBody()?.charStream()?.readText())
            }
        }
    }
}