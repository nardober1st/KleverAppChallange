package com.oechslerbernardo.kleverappchallange.presentation.watchlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oechslerbernardo.kleverappchallange.domain.model.Crypto
import com.oechslerbernardo.kleverappchallange.domain.repository.CryptoRepository
import com.oechslerbernardo.kleverappchallange.presentation.main.MainState
import com.oechslerbernardo.kleverappchallange.util.Resource
import com.oechslerbernardo.kleverappchallange.util.SortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchListViewModel @Inject constructor(
    private val repository: CryptoRepository
) : ViewModel() {

    var state by mutableStateOf(WatchListState())
        private set

    private var _watchListChannelEvent = Channel<WatchListEvent>()
    var watchListChannelEvent = _watchListChannelEvent.receiveAsFlow()

    init {
        loadWatchlist()
    }

    fun onEvent(event: WatchListEvent) {
        when (event) {
            is WatchListEvent.OnAddCryptoNavigate -> {
                viewModelScope.launch {
                    _watchListChannelEvent.send(WatchListEvent.OnAddCryptoNavigate)
                }
            }

            is WatchListEvent.OnCryptoClicked -> {
                viewModelScope.launch {
                    _watchListChannelEvent.send(WatchListEvent.OnCryptoClicked(event.cryptoId))
                }
            }

            is WatchListEvent.OnSortData -> {
                when (event.sortOrder) {
                    SortOrder.ASCENDING -> loadWatchlistSortedByNameAsc()
                    SortOrder.DESCENDING -> loadWatchlistSortedByNameDesc()
                }
            }

            is WatchListEvent.OnRefreshData -> refreshCryptos()
            is WatchListEvent.OnDeleteAllData -> deleteAllCryptos()
        }
    }

    private fun deleteAllCryptos() {
        viewModelScope.launch {
            repository.deleteAllCryptos()
        }
    }

    private fun refreshCryptos() {
        viewModelScope.launch {
            repository.refreshAllCryptos().collect { resource ->
                state = when (resource) {
                    is Resource.Loading -> state.copy(isLoading = true)
                    is Resource.Success -> {
                        loadWatchlist()
                        state.copy(isLoading = false)
                    } // Reload watchlist after refresh
                    is Resource.Error -> {
                        state.copy(error = resource.message, isLoading = false)
                    }
                }
            }
        }
    }

    private fun loadWatchlist() {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            repository.getAllCryptosFromDb().collect { cryptos ->
                state = state.copy(watchlist = cryptos, isLoading = false)
            }
        }
    }

    private fun loadWatchlistSortedByNameAsc() {
        viewModelScope.launch {
            repository.getAllCryptosSortedByNameAsc().collect { sortedList ->
                state = state.copy(watchlist = sortedList, isLoading = false)
            }
        }
    }

    private fun loadWatchlistSortedByNameDesc() {
        viewModelScope.launch {
            repository.getAllCryptosSortedByNameDesc().collect { sortedList ->
                state = state.copy(watchlist = sortedList, isLoading = false)
            }
        }
    }
}