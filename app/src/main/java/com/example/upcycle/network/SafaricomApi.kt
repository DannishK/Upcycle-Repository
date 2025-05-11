package com.example.upcycle.network

import com.example.upcycle.models.AccessTokenResponse
import com.example.upcycle.models.STKPushRequest
import com.example.upcycle.models.STKPushResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface SafaricomApi {
    @POST("mpesa/stkpush/v1/processrequest")
    suspend fun initiateSTKPush(
        @Header("Authorization") token: String,
        @Body stkPush: STKPushRequest
    ): Response<STKPushResponse>
}
interface SafaricomAuthApi {
    @GET("https://sandbox.safaricom.co.ke/oauth/v1/generate")
    suspend fun generateAccessToken(
        @Header("https://sandbox.safaricom.co.ke/Authorization") authHeader: String,
        @Query("https://sandbox.safaricom.co.ke/grant_type") grantType: String = "client_credentials"
    ): Response<AccessTokenResponse>
}
