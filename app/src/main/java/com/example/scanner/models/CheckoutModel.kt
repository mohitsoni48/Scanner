package com.example.scanner.models

data class CheckoutModel(
    val totalCartValue: Double,
    val listOfProducts: List<ProductModel>
)