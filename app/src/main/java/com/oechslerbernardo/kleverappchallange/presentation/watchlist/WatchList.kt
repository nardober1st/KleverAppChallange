package com.oechslerbernardo.kleverappchallange.presentation.watchlist

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.oechslerbernardo.kleverappchallange.presentation.components.CryptoListItem
import com.oechslerbernardo.kleverappchallange.presentation.components.AnimatedEmptyMessage
import com.oechslerbernardo.kleverappchallange.presentation.watchlist.components.WatchListTopAppBar
import com.oechslerbernardo.kleverappchallange.util.SortOrder

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchList(
    state: WatchListState,
    onEvent: (WatchListEvent) -> Unit,
) {
    Scaffold(topBar = {
        WatchListTopAppBar(
            onAddCryptoClick = { onEvent(WatchListEvent.OnAddCryptoNavigate) },
            onDeleteAllClick = { onEvent(WatchListEvent.OnDeleteAllData) },
            onRefreshClick = { onEvent(WatchListEvent.OnRefreshData) },
            onSortAscClick = { onEvent(WatchListEvent.OnSortData(SortOrder.ASCENDING)) },
            onSortDescClick = { onEvent(WatchListEvent.OnSortData(SortOrder.DESCENDING)) }
        )
    }) { padding ->
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

            state.watchlist.isEmpty() -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnimatedEmptyMessage("Your watchlist is empty!")
                    Spacer(modifier = Modifier.height(2.dp))
                    Icon(
                        imageVector = Icons.Filled.HourglassEmpty, contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )
                }
            }

            state.error?.isNotEmpty() == true -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Error: ${state.error}")
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.padding(padding)
                ) {
                    itemsIndexed(state.watchlist) { _, crypto ->
                        CryptoListItem(
                            crypto = crypto,
                            onCryptoClick = { onEvent(WatchListEvent.OnCryptoClicked(crypto.id)) }
                        )
                    }
                }
            }
        }
    }
}