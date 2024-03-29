package com.tekkr.data.internal.common

import com.google.gson.JsonObject
import com.tekkr.data.models.*
import com.tekkr.data.roomDatabase.Item
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface MyApi {

    @POST("api/token/refresh/")
    suspend fun refreshToken(
            @Body request: RequestBody
    ): Response<TokenResponse>

    @POST("api/payments/")
    suspend fun verifyPayment(
            @Body request: RequestBody,
            @Header("Authorization") token: String?
    ): Response<SimpleResponse>

    @POST("api/orders/")
    suspend fun placeOrder(
            @Body request: JsonObject,
            @Header("Authorization") token: String?
    ): Response<Order>

    @GET("api/orders/verify_payment")
    suspend fun verifyPaymentWithOrder(
        @Query("order_id") order_id: String?,
        @Header("Authorization") token: String?
    ): Response<SimpleResponse>

    @GET("api/orders/")
    suspend fun getOrders(
            @Header("Authorization") token: String?
    ): Response<List<Order>>


    @GET("api/items")
    suspend fun getItems(    ): Response<List<Item>>


    @GET("api/sendotp")
    suspend fun sendOtp(
            @Query("phone_number") phone_number: String?
    ): Response<SimpleResponse>


    @GET("api/verifyotp")
    suspend fun verifyOtp(
            @Query("phone_number") phone_number: String?,
            @Query("otp") otp: String?
    ): Response<User>


    companion object {
        operator fun invoke(): MyApi {
            val okkHttpclient = OkHttpClient.Builder()
                    .build()
            return Retrofit.Builder()
                    .client(okkHttpclient)
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(MyApi::class.java)
        }
    }
}
