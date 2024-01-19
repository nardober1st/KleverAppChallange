package com.oechslerbernardo.kleverappchallange.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "crypto_table")
data class CryptoEntity(
    @PrimaryKey val id: Int,
    val circulatingSupply: Double,
    val name: String,
    val slug: String,
    val symbol: String,
    val totalSupply: Double,
    val marketCap: Double,
    val percentChange1h: Double,
    val percentChange24h: Double,
    val percentChange30d: Double,
    val percentChange60d: Double,
    val percentChange7d: Double,
    val percentChange90d: Double,
    val price: Double,
    val volume24h: Double
)