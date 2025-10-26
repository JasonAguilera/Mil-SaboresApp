package com.example.milsaboresapp.domain.model 

data class Product(
    val id: String,
    val name: String,
    val price: Int,
    val imageUrl: String,
    val description: String,
    val category: String
)