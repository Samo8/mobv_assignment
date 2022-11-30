package com.example.assignment.pub_detail

import android.location.Location
import com.example.assignment.pub_detail.model.Element
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

    suspend fun fetchPubsAround(
        location: Location
    ): List<Element> = withContext(Dispatchers.IO) {
        val response = pubDetailService.fetchPubsAround(
            data = "[out:json];node(around:250,${location.latitude}, ${location.longitude});(node(around:250)[\"amenity\"~\"^pub\$|^bar\$|^restaurant\$|^cafe\$|^fast_food\$|^stripclub\$|^nightclub\$\"];);out body;>;out skel;"
        )
        println("URL:" + response.raw().request().url())
        if (response.isSuccessful) {
            val body = response.body()!!.elements
            return@withContext body
        } else {
            throw Exception(response.errorBody()?.charStream()?.readText())
        }
    }
}