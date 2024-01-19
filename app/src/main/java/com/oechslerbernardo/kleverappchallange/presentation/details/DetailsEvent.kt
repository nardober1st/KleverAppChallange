package com.oechslerbernardo.kleverappchallange.presentation.details

import com.oechslerbernardo.kleverappchallange.domain.model.Crypto

sealed class DetailsEvent {
    object OnNavigateBack : DetailsEvent()
    data class OnAddCrypto(val crypto: Crypto) : DetailsEvent()
    data class OnDeleteCrypto(val crypto: Crypto) : DetailsEvent()
}
