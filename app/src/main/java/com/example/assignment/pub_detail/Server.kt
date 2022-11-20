package com.example.assignment.pub_detail

import com.example.assignment.pub_detail.model.PubDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Server {
    private const val URL = "https://overpass-api.de"
    private val pubDetailService: PubDetailService

    init {
        val client = OkHttpClient.Builder().build()
        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        pubDetailService = retrofit.create(PubDetailService::class.java)
    }

    suspend fun fetchPubDetail(pubId: String): PubDetail = withContext(Dispatchers.IO) {
        val response = pubDetailService.fetchPubDetail(
            data = "[out:json];node(${pubId});out body;>;out skel;"
        )
        println("URL:" + response.raw().request().url())
        if (response.isSuccessful) {
            val body = response.body()!!
            return@withContext body
        } else {
            throw Exception(response.errorBody()?.charStream()?.readText())
        }
    }
}