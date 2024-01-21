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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.oechslerbernardo.kleverappchallange.presentation.components.CryptoListItem
import com.oechslerbernardo.kleverappchallange.presentation.components.AnimatedEmptyMessage
import com.oechslerbernardo.kleverappchallange.presentation.components.DisplayAlertDialog
import com.oechslerbernardo.kleverappchallange.presentation.watchlist.components.WatchListTopAppBar
import com.oechslerbernardo.kleverappchallange.util.SortOrder
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchList(
    snackBar: SnackbarHostState,
    state: WatchListState,
    onEvent: (WatchListEvent) -> Unit,
) {

    var openDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            WatchListTopAppBar(
                onAddCryptoClick = { onEvent(WatchListEvent.OnAddCryptoNavigate) },
                onDeleteAllClick = {
                    if (state.watchlist.isNotEmpty()) {
                        openDialog = true
                    } else {
                        scope.launch {
                            snackBar.showSnackbar("Your watchlist is already empty!")
                        }
                    }
                },
                onRefreshClick = {
                    if (state.watchlist.isNotEmpty()) {
                        onEvent(WatchListEvent.OnRefreshData)
                        scope.launch {
                            snackBar.showSnackbar("Data refreshed!")
                        }
                    } else {
                        scope.launch {
                            snackBar.showSnackbar("No data to be refreshed!")
                        }
                    }
                },
                onSortAscClick = { onEvent(WatchListEvent.OnSortData(SortOrder.ASCENDING)) },
                onSortDescClick = { onEvent(WatchListEvent.OnSortData(SortOrder.DESCENDING)) }
            )
            DisplayAlertDialog(
                title = "Delete All",
                message = "Are you sure you want to permanently delete all crypto coins from your watchlist?",
                dialogOpened = openDialog,
                onDialogClosed = { openDialog = false },
                onYesClicked = {
                    onEvent(WatchListEvent.OnDeleteAllData)
                    scope.launch {
                        snackBar.showSnackbar("All cryptos coins were deleted from your watchlist!")
                    }
                })
        },
        snackbarHost = { SnackbarHost(hostState = snackBar) }
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