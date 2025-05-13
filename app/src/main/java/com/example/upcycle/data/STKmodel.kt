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
    //accountRef: String
) {
    // Safaricom Business details
    val businessShortCode = "174379"
    val passKey = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919"

    // Generate Timestamp in the required format
    val timestamp = getCurrentTimestamp()

    // Generate Password for STK Push
    val password = generatePassword(businessShortCode, passKey, timestamp)

    // Format Phone Number to international format (e.g., 2547xxxxxxxx)
    val formattedPhone = if (phoneNumber.startsWith("07")) {
        "254" + phoneNumber.drop(1)
    } else {
        phoneNumber
    }
    val callBackURL = "https://webhook.site/276b957c-5774-4c77-966d-1b44e4b54191"
    val accountReference = "UPCYCLE"
    if (!validateSTKPushRequest(formattedPhone, amount, businessShortCode, password, timestamp, accountReference, callBackURL)) {
        Toast.makeText(context, "Validation failed. Check Logcat for details.", Toast.LENGTH_LONG).show()
        return
    }

    // Construct the STK Push request
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
        .baseUrl("https://sandbox.safaricom.co.ke/")
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
                    // 🔴 Log the Error Body for troubleshooting
                    val errorBody = response.errorBody()?.string()
                    Log.e("STK_PUSH_ERROR", "Error Body: $errorBody")
                    Toast.makeText(
                        context,
                        "STK Push failed: $errorBody",
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
    dateFormat.timeZone = TimeZone.getTimeZone("Africa/Nairobi")
    return dateFormat.format(Date())
}

fun generatePassword(businessShortCode: String, passKey: String, timestamp: String): String {
    val toEncode = businessShortCode + passKey + timestamp
    return Base64.encodeToString(toEncode.toByteArray(), Base64.NO_WRAP)
}

suspend fun getAccessToken(): String? {
    val consumerKey = "ojNFhTcD7XfxiJz6HTRuERQraqM1QSalu53O7A0KlxrYxEA7"
    val consumerSecret = "97XjHGA92XS8vNlAF2ElUJEcC2jrj9esboghg1Xxq1GK5QwfCMPR75ZQsr7g3isl"

    // Encoding the consumer key and secret
    val auth = Base64.encodeToString("$consumerKey:$consumerSecret".toByteArray(), Base64.NO_WRAP)
    Log.d("AUTH_HEADER", auth) // Log the encoded value

    // Retrofit setup
    val retrofit = Retrofit.Builder()
        .baseUrl("https://sandbox.safaricom.co.ke/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val safaricomAuthApi = retrofit.create(SafaricomAuthApi::class.java)

    return try {
        // Make the API call
        val response = safaricomAuthApi.generateAccessToken("Basic $auth")

        Log.d("FULL_RESPONSE", response.toString())

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
fun validateSTKPushRequest(
    phoneNumber: String,
    amount: String,
    businessShortCode: String,
    password: String,
    timestamp: String,
    accountReference: String,
    callbackUrl: String
): Boolean {
    // Validate Phone Number
    if (!phoneNumber.matches(Regex("^2547[0-9]{8}$"))) {
        Log.e("STK_VALIDATION", "Phone number must be in the format 2547XXXXXXXX.")
        return false
    }

    // Validate Amount
    if (amount.toIntOrNull() == null || amount.toInt() <= 0) {
        Log.e("STK_VALIDATION", "Amount must be a positive number.")
        return false
    }

    // Validate BusinessShortCode
    if (businessShortCode.isEmpty() || businessShortCode.length != 6) {
        Log.e("STK_VALIDATION", "Invalid BusinessShortCode.")
        return false
    }

    // Validate Password
    if (password.isEmpty()) {
        Log.e("STK_VALIDATION", "Password generation failed.")
        return false
    }

    // Validate Timestamp
    if (!timestamp.matches(Regex("^\\d{14}$"))) {
        Log.e("STK_VALIDATION", "Timestamp must be in the format yyyyMMddHHmmss.")
        return false
    }

    // Validate Account Reference
    if (accountReference.isEmpty()) {
        Log.e("STK_VALIDATION", "Account Reference cannot be empty.")
        return false
    }

    // Validate Callback URL
    if (!callbackUrl.startsWith("https://")) {
        Log.e("STK_VALIDATION", "Callback URL must be a secure HTTPS endpoint.")
        return false
    }

    Log.d("STK_VALIDATION", "All fields are valid.")
    return true
}

