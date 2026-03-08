package com.example.overwatchapp

data class Hero(
    val id: String,
    val name: String,
    val imageRes: Int,
    val imageUrl: String? = null
)
