package com.oechslerbernardo.kleverappchallange.presentation.main

import com.oechslerbernardo.kleverappchallange.domain.model.Crypto

data class MainState(
    val cryptos: List<Crypto> = emptyList(),
    val currentPage: Int = 1,
    val selectedCrypto: Crypto? = null,
    val pageSize: Int = 100, // Default value; adjust as needed
    val isLoading: Boolean = false,
    val error: String? = null,
    val sortOption: Pair<String?, String?>? = null
)