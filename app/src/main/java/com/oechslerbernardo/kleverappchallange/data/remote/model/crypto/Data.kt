package com.oechslerbernardo.kleverappchallange.data.remote.model.crypto

data class Data(
    val circulating_supply: Double,
    val id: Int,
    val name: String,
    val quote: Quote,
    val slug: String,
    val symbol: String,
    val total_supply: Double
)