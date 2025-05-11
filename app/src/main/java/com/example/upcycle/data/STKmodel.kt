package com.example.upcycle.data

import android.content.Context
import android.widget.Toast
import com.example.upcycle.models.STKPushRequest
import com.example.upcycle.network.SafaricomApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import android.util.Base64
import android.util.Log
import com.example.upcycle.network.SafaricomAuthApi
import java.util.Date
import java.util.Locale
import java.util.TimeZone


fun initiateSTKPush(
    context: Context,
    phoneNumber: String,
    amount: String,
    accountReference: String,
    accountRef: String
) {
    val businessShortCode = "174379"
    val passKey = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919"
    val timestamp = getCurrentTimestamp()
    val password = generatePassword(businessShortCode, passKey, timestamp)
    val formattedPhone = if (phoneNumber.startsWith("07")) {
        "254" + phoneNumber.drop(1)
    } else {
        phoneNumber
    }


    val stkPushRequest = STKPushRequest(
        BusinessShortCode = businessShortCode,
        Password = password,
        Timestamp = timestamp,
        Amount = amount,
        PartyA = formattedPhone,
        PartyB = businessShortCode,
        PhoneNumber = formattedPhone,
        CallBackURL = "https://webhook.site/276b957c-5774-4c77-966d-1b44e4b54191",
        AccountReference = accountReference,
        TransactionDesc = "Payment for $accountReference"
    )

    // Initialize Retrofit
    val retrofit = Retrofit.Builder()
        .baseUrl("https://sandbox.safaricom.co.ke/")  // Use the sandbox for testing
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val safaricomApi = retrofit.create(SafaricomApi::class.java)

    // Coroutine to call the API
    CoroutineScope(Dispatchers.IO).launch {
        val token = getAccessToken()
        if (token != null) {
            val response = safaricomApi.initiateSTKPush("Bearer $token", stkPushRequest)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        context,
                        "STK Push initiated. Check your phone.",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        "STK Push failed: ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        } else {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Failed to generate access token", Toast.LENGTH_LONG).show()
            }
        }
    }

}
    fun getCurrentTimestamp(): String {
    val dateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("Africa/Nairobi") // Ensure it matches Kenyan timezone
    return dateFormat.format(Date())
}
fun generatePassword(businessShortCode: String, passKey: String, timestamp: String): String {
    val passwordString = businessShortCode + passKey + timestamp
    return Base64.encodeToString(passwordString.toByteArray(), Base64.NO_WRAP)
}
suspend fun getAccessToken(): String? {
    val consumerKey = "ojNFhTcD7XfxiJz6HTRuERQraqM1QSa"
    val consumerSecret = "97XjHGA92XS8vNIAF2EIUJECc2rj9est"

    val auth =Base64.encodeToString("$consumerKey:$consumerSecret".toByteArray(), Base64.NO_WRAP)

    val retrofit = Retrofit.Builder()
        .baseUrl("https://sandbox.safaricom.co.ke/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val safaricomAuthApi = retrofit.create(SafaricomAuthApi::class.java)

    return try {
        val response = safaricomAuthApi.generateAccessToken("Basic $auth")
        if (response.isSuccessful) {
            Log.d("ACCESS_TOKEN_SUCCESS", "Token received: ${response.body()?.accessToken}")
            response.body()?.accessToken
        } else {
            val errorBody = response.errorBody()?.string()
            Log.e("ACCESS_TOKEN_ERROR", "Failed with error: ${response.message()}")
            Log.e("ACCESS_TOKEN_ERROR", "Error Body: $errorBody")
            null
        }
    } catch (e: Exception) {
        Log.e("ACCESS_TOKEN_EXCEPTION", "Exception: ${e.message}")
        null
    }
}