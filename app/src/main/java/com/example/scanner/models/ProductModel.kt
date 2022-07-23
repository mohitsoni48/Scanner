package com.example.scanner.models

data class ProductModel(
    val id: Int,
    val productName: String,
    val productDescription: String,
    val productPrice: Double,
    val productImageUrl: String,
    var qty: Int = 0,
)
