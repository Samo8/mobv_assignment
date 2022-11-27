package com.example.assignment.server

import com.example.assignment.PubsService
import com.example.assignment.data.api.JoinPubRequest
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

    suspend fun joinPub (
        body: JoinPubRequest,
    ): Unit = withContext(Dispatchers.IO) {
//        val response = pubsService.joinPub(
//            headers = mapOf(
//                "authorization" to "Bearer ${authData.access}",
//                "x-apikey" to "c95332ee022df8c953ce470261efc695ecf3e784",
//                "x-user" to authData.uid,
//            ),
//            body = body,
//        )
//        if (response.code() == 401) {
//            try {
////                refresh(authData.uid, authData.refresh, sessionManager)
//                joinPub(sessionManager, body)
//            } catch (e: Exception) {
//                throw Exception(e.toString())
//            }
//        } else {
//            if (!response.isSuccessful) {
//                println(response.errorBody())
//                println(response.errorBody()?.charStream()?.readText())
//                throw Exception(response.errorBody()?.charStream()?.readText())
//            }
//        }
    }
}