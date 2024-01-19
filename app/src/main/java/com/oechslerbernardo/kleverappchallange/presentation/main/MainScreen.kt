package com.oechslerbernardo.kleverappchallange.presentation.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.SignalWifiConnectedNoInternet4
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.oechslerbernardo.kleverappchallange.presentation.components.CryptoListItem
import com.oechslerbernardo.kleverappchallange.presentation.main.components.CryptoListTopBar
import com.oechslerbernardo.kleverappchallange.presentation.components.AnimatedEmptyMessage
import com.oechslerbernardo.kleverappchallange.presentation.other.EmptyStateScreen
import com.oechslerbernardo.kleverappchallange.presentation.other.NetworkErrorScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(state: MainState, onEvent: (MainEvent) -> Unit) {

    val listState = rememberLazyListState()
    val showMenu = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CryptoListTopBar(
                onBackPress = { onEvent(MainEvent.OnNavigateBack) },
                onRefresh = { onEvent(MainEvent.OnRefreshScreen) },
                onSort = { showMenu.value = true },
                showMenu = showMenu,
                onSortOptionSelected = { sort, sortDir ->
                    onEvent(MainEvent.OnSortList(sort, sortDir))
                }
            )
        },
    ) { padding ->
        when {
            state.isLoading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                NetworkErrorScreen()
            }

            state.cryptos.isNotEmpty() -> {
                LazyColumn(
                    state = listState, modifier = Modifier.padding(padding)
                ) {
                    itemsIndexed(state.cryptos) { index, crypto ->
                        CryptoListItem(crypto = crypto, onCryptoClick = { selectedCrypto ->
                            onEvent(MainEvent.OnCryptoClicked(selectedCrypto))
                        })
                        if (index == state.cryptos.size - 1) {
                            LaunchedEffect(key1 = state.cryptos.size) {
                                onEvent(MainEvent.OnNextPageLoad)
                            }
                        }
                    }
                }
            }

            else -> {
                EmptyStateScreen()
            }
        }
    }
}
