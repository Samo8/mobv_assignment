package com.example.assignment

import android.util.Log
import com.example.assignment.common.PubData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Server {
    private const val URL = "https://data.mongodb-api.com"
    private val pubsService: PubsService

    init {
        val client = OkHttpClient.Builder().build()
        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        pubsService = retrofit.create(PubsService::class.java)
    }

    private val tag = Server::class.java.name
    suspend fun post(): PubData = withContext(Dispatchers.IO) {
        Log.d(tag, "Thread is ${Thread.currentThread().name}")
        val request = PubsService.PostRequest(
            collection = "bars",
            database = "mobvapp",
            dataSource = "Cluster0"
        )

        val response = pubsService.post(request)
        if (response.isSuccessful) {
            val body = response.body()!!
            println(body.elements)
            return@withContext body
        } else {
            throw Exception(response.errorBody()?.charStream()?.readText())
        }
    }
}