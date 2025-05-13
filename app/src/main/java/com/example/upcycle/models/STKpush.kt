package com.example.upcycle.models

import com.google.gson.annotations.SerializedName

data class STKPushRequest(
    @SerializedName("BusinessShortCode") val BusinessShortCode: String,
    @SerializedName("Password") val Password: String,
    @SerializedName("Timestamp") val Timestamp: String,
    @SerializedName("TransactionType") val TransactionType: String = "CustomerPayBillOnline",
    @SerializedName("Amount") val Amount: String,
    @SerializedName("PartyA") val PartyA: String,
    @SerializedName("PartyB") val PartyB: String,
    @SerializedName("PhoneNumber") val PhoneNumber: String,
    @SerializedName("CallBackURL") val CallBackURL: String,
    @SerializedName("AccountReference") val AccountReference: String,
    @SerializedName("TransactionDesc") val TransactionDesc: String
)


data class STKPushResponse(
    @SerializedName("MerchantRequestID") val MerchantRequestID: String?,
    @SerializedName("CheckoutRequestID") val CheckoutRequestID: String?,
    @SerializedName("ResponseCode") val ResponseCode: String?,
    @SerializedName("ResponseDescription") val ResponseDescription: String?,
    @SerializedName("CustomerMessage") val CustomerMessage: String?
)
data class AccessTokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("expires_in") val expiresIn: String
)
