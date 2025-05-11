package com.example.upcycle.models

import com.google.gson.annotations.SerializedName

data class STKPushRequest(
    val BusinessShortCode: String,
    val Password: String,
    val Timestamp: String,
    val TransactionType: String = "CustomerPayBillOnline",
    val Amount: String,
    val PartyA: String,
    val PartyB: String,
    val PhoneNumber: String,
    val CallBackURL: String,
    val AccountReference: String,
    val TransactionDesc: String
)

data class STKPushResponse(
    val MerchantRequestID: String,
    val CheckoutRequestID: String,
    val ResponseCode: String,
    val ResponseDescription: String,
    val CustomerMessage: String
)
data class AccessTokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("expires_in") val expiresIn: String
)
