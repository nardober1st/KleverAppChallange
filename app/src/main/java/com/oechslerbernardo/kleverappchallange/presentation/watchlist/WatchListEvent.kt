package com.oechslerbernardo.kleverappchallange.presentation.watchlist

import com.oechslerbernardo.kleverappchallange.util.SortOrder

sealed class WatchListEvent {
    object OnAddCryptoNavigate : WatchListEvent()
    object OnRefreshData : WatchListEvent()
    object OnDeleteAllData : WatchListEvent()
    data class OnCryptoClicked(val cryptoId: Int) : WatchListEvent()
    data class OnSortData(val sortOrder: SortOrder) : WatchListEvent()
}
