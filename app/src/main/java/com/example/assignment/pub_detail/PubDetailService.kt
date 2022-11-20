package com.example.assignment.pub_detail

import com.example.assignment.pub_detail.model.PubDetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PubDetailService {
    @GET("/api/interpreter")
    suspend fun fetchPubDetail(
        @Query("data") data: String,
    ): Response<PubDetail>
}