package com.oechslerbernardo.kleverappchallange.presentation.main

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oechslerbernardo.kleverappchallange.data.repository.CryptoRepositoryImpl
import com.oechslerbernardo.kleverappchallange.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: CryptoRepositoryImpl
) : ViewModel() {

    var state by mutableStateOf(MainState())
        private set

    private var _mainChannelEvent = Channel<MainEvent>()
    var mainChannelEvent = _mainChannelEvent.receiveAsFlow()

    init {
        loadCryptos(state.currentPage, state.pageSize)
    }

    fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.OnNavigateBack -> {
                viewModelScope.launch {
                    _mainChannelEvent.send(MainEvent.OnNavigateBack)
                }
            }

            is MainEvent.OnRefreshScreen -> {
                refreshCryptosFromApi()
            }

            is MainEvent.OnNextPageLoad -> {
                loadNextPage()
            }

            is MainEvent.OnSortList -> {
                setSortOption(event.sort, event.sortDir)
            }

            is MainEvent.OnCryptoClicked -> {
                viewModelScope.launch {
                    _mainChannelEvent.send(MainEvent.OnCryptoClicked(event.cryptoId))
                }
            }
        }
    }

    private fun setSortOption(sort: String, sortDir: String) {
        Log.d("TAGY", "Setting sort option: $sort, $sortDir")
        state = state.copy(
            sortOption = sort to sortDir, currentPage = 1
        )
        refreshCryptos()
    }

    private fun loadCryptos(page: Int, limit: Int) {
        Log.d(
            "TAGY",
            "Loading page: $page, sort: ${state.sortOption?.first}, direction: ${state.sortOption?.second}"
        )
        viewModelScope.launch {
//            state = state.copy(isLoading = true)
            repository.getAllCryptos(page, limit, state.sortOption?.first, state.sortOption?.second)
                .collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            val newCryptos = resource.data
                            state = if (page == 1) {
                                state.copy(cryptos = newCryptos, isLoading = false)
                            } else {
                                state.copy(cryptos = state.cryptos + newCryptos, isLoading = false)
                            }
                        }

                        is Resource.Error -> {
                            state = state.copy(error = resource.message, isLoading = false)
                        }

                        is Resource.Loading -> {
                            state = state.copy(isLoading = true)
                        }
                    }
                }
        }
    }

    private fun refreshCryptosFromApi() {
        viewModelScope.launch {
            repository.getAllCryptos(state.currentPage, state.pageSize, state.sortOption?.first, state.sortOption?.second)
                .onStart {
                    state = state.copy(isLoading = true, error = null)
                }
                .catch { e ->
                    state = state.copy(isLoading = false, error = e.localizedMessage)
                }
                .collect { resource ->
                    state = when (resource) {
                        is Resource.Success -> {
                            state.copy(cryptos = resource.data, isLoading = false)
                        }

                        is Resource.Error -> {
                            state.copy(isLoading = false, error = resource.message)
                        }

                        is Resource.Loading -> {
                            state.copy(isLoading = true, error = null)
                        }
                    }
                }
        }
    }

    private fun refreshCryptos() {
        state = state.copy(
            currentPage = 1,
            cryptos = emptyList(),
            isLoading = true
        )
        loadCryptos(state.currentPage, state.pageSize)
    }

    private fun loadNextPage() {
        state = state.copy(currentPage = state.currentPage + 1)
        loadCryptos(state.currentPage, state.pageSize)
    }
}
