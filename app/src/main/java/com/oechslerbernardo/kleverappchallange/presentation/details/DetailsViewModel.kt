package com.oechslerbernardo.kleverappchallange.presentation.details

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oechslerbernardo.kleverappchallange.data.repository.CryptoRepositoryImpl
import com.oechslerbernardo.kleverappchallange.domain.model.Crypto
import com.oechslerbernardo.kleverappchallange.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: CryptoRepositoryImpl
) : ViewModel() {

    var state by mutableStateOf(DetailsState())
        private set

    private var _detailsChannelEvent = Channel<DetailsEvent>()
    var detailsChannelEvent = _detailsChannelEvent.receiveAsFlow()

    init {
        getCryptoIdArgument()
        getSelectedCrypto()
    }

    fun onEvent(event: DetailsEvent) {
        when (event) {
            is DetailsEvent.OnNavigateBack -> {
                viewModelScope.launch {
                    _detailsChannelEvent.send(DetailsEvent.OnNavigateBack)
                }
            }

            is DetailsEvent.OnAddCrypto -> {
                viewModelScope.launch {
                    _detailsChannelEvent.send(DetailsEvent.OnAddCrypto(event.crypto))
                    addCryptoToWatchlist(event.crypto)
                }
            }

            is DetailsEvent.OnDeleteCrypto -> {
                viewModelScope.launch {
                    _detailsChannelEvent.send(DetailsEvent.OnDeleteCrypto(event.crypto))
                    removeCryptoFromWatchlist(event.crypto)
                }
            }
        }
    }

    private fun getCryptoIdArgument() {
        val cryptoId = savedStateHandle.get<Int>(key = "cryptoId")
        val isFromDb = savedStateHandle.get<Boolean>(key = "isFromDb")
        Log.d("TAGY", "Received crypto: $cryptoId and $isFromDb")
        state = state.copy(
            selectedCryptoId = cryptoId,
            isFromDb = isFromDb
        )
    }

    private fun getSelectedCrypto() {
        val cryptoId = state.selectedCryptoId
        val isFromDb = state.isFromDb

        if (cryptoId != null) {
            viewModelScope.launch {
//                state = state.copy(isLoading = true)
                if (isFromDb == true) {
                    state = try {
                        val crypto = repository.getCryptoById(cryptoId)
                        checkIfCryptoIsInWatchlist(cryptoId)
                        state.copy(selectedCrypto = crypto, isLoading = false)
                    } catch (e: Exception) {
                        state.copy(error = e.localizedMessage, isLoading = false)
                    }
                } else {
                    // Fetch crypto details from the API
                    repository.getSelectedCrypto(cryptoId).collect { resource ->
                        state = when (resource) {
                            is Resource.Success -> {
                                checkIfCryptoIsInWatchlist(cryptoId)
                                state.copy(selectedCrypto = resource.data, isLoading = false)
                            }

                            is Resource.Error -> {
                                state.copy(error = resource.message, isLoading = false)
                            }

                            is Resource.Loading -> {
                                state.copy(isLoading = true)
                            }
                        }
                    }
                }
            }
        }
    }


    private fun addCryptoToWatchlist(crypto: Crypto) {
        viewModelScope.launch {
            repository.addCrypto(crypto)
            state = state.copy(isCryptoAddedToWatchlist = true)
        }
    }

    private fun removeCryptoFromWatchlist(crypto: Crypto) {
        viewModelScope.launch {
            repository.deleteCrypto(crypto)
            state = state.copy(isCryptoAddedToWatchlist = false)
        }
    }

    private fun checkIfCryptoIsInWatchlist(cryptoId: Int) {
        viewModelScope.launch {
            val crypto = repository.getCryptoById(cryptoId)
            state = state.copy(isCryptoAddedToWatchlist = crypto != null)
        }
    }
}