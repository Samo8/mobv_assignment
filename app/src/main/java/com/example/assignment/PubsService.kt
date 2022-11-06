package com.example.assignment

import com.example.assignment.common.PubData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface PubsService {
    data class PostRequest(
        val collection: String,
        val database: String,
        val dataSource: String,
    )

    @Headers("api-key: KHUu1Fo8042UwzczKz9nNeuVOsg2T4ClIfhndD2Su0G0LHHCBf0LnUF05L231J0M")
    @POST("/app/data-fswjp/endpoint/data/v1/action/find")
    suspend fun post(@Body request: PostRequest): Response<PubData>
}