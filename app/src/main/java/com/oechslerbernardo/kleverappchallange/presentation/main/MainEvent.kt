package com.oechslerbernardo.kleverappchallange.presentation.main

import com.oechslerbernardo.kleverappchallange.domain.model.Crypto

sealed class MainEvent {
    object OnNavigateBack : MainEvent()
    object OnRefreshScreen : MainEvent()
    object OnNextPageLoad : MainEvent()
    data class OnSortList(val sort:String, val sortDir: String) : MainEvent()
    data class OnCryptoClicked(val cryptoId: Int) : MainEvent()
}
