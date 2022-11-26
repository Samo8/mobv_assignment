package com.example.assignment.server

import com.example.assignment.PubsService
import com.example.assignment.SessionManager
import com.example.assignment.common.PubData
import com.example.assignment.auth.AuthData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object MpageServer {
    private const val URL = "https://zadanie.mpage.sk"
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
//                val updatedAuthData = refresh(authData.uid, authData.refresh, sessionManager)
                fetchBarList(authData, sessionManager)
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

    suspend fun joinPub (
        sessionManager: SessionManager,
        body: PubsService.JoinPubRequest,
    ): Unit = withContext(Dispatchers.IO) {
        val authData = sessionManager.fetchAuthData()
        val response = pubsService.joinPub(
            headers = mapOf(
                "authorization" to "Bearer ${authData.access}",
                "x-apikey" to "c95332ee022df8c953ce470261efc695ecf3e784",
                "x-user" to authData.uid,
            ),
            body = body,
        )
        if (response.code() == 401) {
            try {
//                refresh(authData.uid, authData.refresh, sessionManager)
                joinPub(sessionManager, body)
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
}