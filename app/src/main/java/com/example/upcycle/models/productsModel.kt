package com.example.upcycle.models

import com.google.firebase.Timestamp

data class ProductsModel(
    val id: String = "",
    val name: String = "",
    val price: String = "",
    val description: String = "",
    val category: String = "",
    val imageUrl: String= "",//List<String> = listOf(),
    val sellerId: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    var userId: String="",
    val location: String = ""

)