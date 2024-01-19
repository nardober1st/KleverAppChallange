package com.oechslerbernardo.kleverappchallange.presentation.watchlist

import com.oechslerbernardo.kleverappchallange.domain.model.Crypto

data class WatchListState(
    val watchlist: List<Crypto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)