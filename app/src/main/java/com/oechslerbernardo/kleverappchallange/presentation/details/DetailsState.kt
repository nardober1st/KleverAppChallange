package com.oechslerbernardo.kleverappchallange.presentation.details

import com.oechslerbernardo.kleverappchallange.domain.model.Crypto

data class DetailsState(
    val selectedCryptoId: Int? = null,
    val isLoading: Boolean = false,
    val isCryptoAddedToWatchlist: Boolean = false,
    val selectedCrypto: Crypto? = null,
    val error: String? = null,
    val isFromDb: Boolean? = null
)