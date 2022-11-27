package com.example.assignment.data.api

import android.content.Context
import com.example.assignment.auth.AuthData
import com.example.assignment.common.PubData
import com.example.assignment.data.api.interceptors.AuthInterceptor
import com.example.assignment.data.api.interceptors.TokenAuthenticator
import com.example.assignment.pub_detail.model.PubDetail
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RestApi {

    @GET("https://overpass-api.de/api/interpreter?")
    suspend fun fetchPubDetail(
        @Query("data") data: String,
    ): Response<PubDetail>

    @GET("https://overpass-api.de/api/interpreter?")
    suspend fun fetchPubsAround(
        @Query("data") data: String
    ): Response<PubsAroundResponse>

    @POST("user/create.php")
    suspend fun userCreate(@Body user: UserSignRequest): Response<AuthData>

    @POST("user/login.php")
    suspend fun userLogin(@Body user: UserSignRequest): Response<AuthData>

    @POST("user/refresh.php")
    fun userRefresh(@Body user: RefreshTokenRequest) : Call<AuthData>

    @GET("bar/list.php")
    @Headers("mobv-auth: accept")
    suspend fun barList() : Response<List<PubData>>

    @POST("/contact/message.php")
    @Headers("mobv-auth: accept")
    suspend fun addFriend(
        @Body request: AddFriendRequest,
    ): Response<Void>

    @GET("/contact/list.php")
    @Headers("mobv-auth: accept")
    suspend fun fetchFriends(): Response<List<Friend>>

//    @POST("bar/message.php")
//    @Headers("mobv-auth: accept")
//    suspend fun barMessage(@Body bar: BarMessageRequest) : Response<Any>

    companion object{
        private const val BASE_URL = "https://zadanie.mpage.sk/"

        fun create(context: Context): RestApi {
            val client = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(context))
                .authenticator(TokenAuthenticator(context))
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(RestApi::class.java)
        }
    }
}