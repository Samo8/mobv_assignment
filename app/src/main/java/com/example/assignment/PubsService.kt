package com.example.assignment

import com.example.assignment.common.PubData
import com.example.assignment.data.api.JoinPubRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface PubsService {
    @GET("/bar/list.php")
    suspend fun fetchBarList(
        @HeaderMap headers: Map<String, String>,
    ): Response<List<PubData>>

    @POST("/bar/message.php")
    suspend fun joinPub(
        @HeaderMap headers: Map<String, String>,
        @Body body: JoinPubRequest,
    ): Response<Void>
}