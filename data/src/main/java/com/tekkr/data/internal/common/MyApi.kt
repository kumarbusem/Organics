package com.tekkr.data.internal.common

import com.tekkr.data.models.*
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface MyApi {

    @POST("riders/login/")
    suspend fun riderLogin(
            @Body request: RequestBody
    ): Response<User>

    @POST("api/v1/login/")
    suspend fun fpsLogin(
            @Body request: RequestBody
    ): Response<User>

    @GET("api/v1/get_orders/")
    suspend fun getOrders(
            @Header("Authorization") token: String?
    ): Response<List<Order>>

    @GET("api/v1/fps_delivered_orders/")
    suspend fun getFpsOrders(
            @Header("Authorization") token: String?
    ): Response<List<Order>>

    @GET("api/items")
    suspend fun getItems(    ): Response<List<Item>>

    @GET("api/v1/search_ration_id/")
    suspend fun getOrder(
            @Query("ration_id") ration_id: String?,
            @Header("Authorization") token: String?
    ): Response<OrderResponse>

    @PUT("spanel/close_runsheet/")
    suspend fun closeRunsheet(
            @Query("id") id: String?,
            @Header("Authorization") token: String?
    ): Response<SimpleResponse>

    @GET("riders/profile_otp/")
    suspend fun sendOtp(
            @Query("ph_num") phone: String?,
            @Header("Authorization") token: String?
    ): Response<SimpleResponse>

    @POST("riders/verify_profile_otp/")
    suspend fun verifyOtp(
            @Body request: RequestBody,
            @Header("Authorization") token: String?
    ): Response<SimpleResponse>

    @POST("spanel/pickup_otp/")
    suspend fun updatePickupOTP(
            @Body request: RequestBody,
            @Header("Authorization") token: String?
    ): Response<SimpleResponse>

    @POST("spanel/ref_no_verification/")
    suspend fun refVerifiedPost(
            @Body request: RequestBody,
            @Header("Authorization") token: String?
    ): Response<SimpleResponse>

    @POST("spanel/ce_selfie_upload/")
    fun uploadSelfie(
            @Body file: RequestBody?,
            @Header("Authorization") token: String?
    ): Call<SimpleResponse>

    @POST("api/v1/order_status_update/")
    fun updateOrder(
            @Body file: RequestBody?,
            @Header("Authorization") token: String?
    ): Call<SimpleResponse>

    @POST("api/v1/fps_order_status/")
    fun updateOrderFps(
            @Body file: RequestBody?,
            @Header("Authorization") token: String?
    ): Call<SimpleResponse>

    @POST("spanel/deposit_image_upload/")
    fun uploadDeposit(
            @Body file: RequestBody?,
            @Header("Authorization") token: String?
    ): Call<SimpleResponse>

    @POST("spanel/profile_pic_upload/")
    fun uploadProfilePic(
            @Body file: RequestBody?,
            @Header("Authorization") token: String?
    ): Call<ProfilePicResponse>

    @POST("riders/change_password/")
    suspend fun changePassword(
            @Body request: RequestBody,
            @Header("Authorization") token: String?
    ): Response<SimpleResponse>

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
