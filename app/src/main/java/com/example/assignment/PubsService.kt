package com.example.assignment

import com.example.assignment.common.PubData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap

interface PubsService {
    @GET("/bar/list.php")
    suspend fun fetchBarList(
        @HeaderMap headers: Map<String, String>,
    ): Response<List<PubData>>
}