package com.oechslerbernardo.kleverappchallange.presentation.details

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.oechslerbernardo.kleverappchallange.presentation.details.components.DetailCryptoTopAppBar
import com.oechslerbernardo.kleverappchallange.presentation.main.MainState
import com.oechslerbernardo.kleverappchallange.ui.theme.Green1
import com.oechslerbernardo.kleverappchallange.ui.theme.Red1
import com.oechslerbernardo.kleverappchallange.util.formatChangeAmount
import com.oechslerbernardo.kleverappchallange.util.formatLargeNumber
import com.oechslerbernardo.kleverappchallange.util.formatPercentageChange
import com.oechslerbernardo.kleverappchallange.util.formatPrice

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CryptoDetailScreen(state: DetailsState, onEvent: (DetailsEvent) -> Unit) {

    Scaffold(
        topBar = {
            val title = state.selectedCrypto?.name ?: "Crypto Details"
            val actionIcon = if (state.isCryptoAddedToWatchlist) {
                Icons.Filled.Check
            } else {
                Icons.Filled.Add
            }
            DetailCryptoTopAppBar(
                title = title,
                navigationIconOnClick = { onEvent(DetailsEvent.OnNavigateBack) },
                actionIcon = actionIcon,
                onActionClick = {
                    state.selectedCrypto?.let { crypto ->
                        if (state.isCryptoAddedToWatchlist) {
                            onEvent(DetailsEvent.OnDeleteCrypto(crypto))
                        } else {
                            onEvent(DetailsEvent.OnAddCrypto(crypto))
                        }
                    }
                },
                actionIconDescription = "Toggle Watchlist"
            )
        }) { padding ->
        if (state.isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        } else if (state.selectedCrypto != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = state.selectedCrypto.symbol,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 14.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    modifier = Modifier.alpha(0.50f),
                    text = state.selectedCrypto.name,
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatPrice(state.selectedCrypto.price),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(
                        text = formatChangeAmount(
                            state.selectedCrypto.price,
                            state.selectedCrypto.percentChange24h
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        color = if ((state.selectedCrypto.percentChange24h) >= 0) Green1 else Red1
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = formatPercentageChange(state.selectedCrypto.percentChange24h),
                        style = MaterialTheme.typography.titleMedium,
                        color = if ((state.selectedCrypto.percentChange24h) >= 0) Green1 else Red1
                    )
                }
                Spacer(modifier = Modifier.height(100.dp))
                DetailRow("Total Supply:", formatLargeNumber(state.selectedCrypto.totalSupply))
                DetailRow("Market Cap:", formatPrice(state.selectedCrypto.marketCap))
                DetailRow(
                    "Change (1h):",
                    formatPercentageChange(state.selectedCrypto.percentChange1h),
                    valueColor = if ((state.selectedCrypto.percentChange1h) >= 0) Green1 else Red1
                )
                DetailRow(
                    "Change (24h):",
                    formatPercentageChange(state.selectedCrypto.percentChange24h),
                    valueColor = if ((state.selectedCrypto.percentChange24h) >= 0) Green1 else Red1
                )
                DetailRow(
                    "Change (7d):",
                    formatPercentageChange(state.selectedCrypto.percentChange7d),
                    valueColor = if ((state.selectedCrypto.percentChange7d) >= 0) Green1 else Red1
                )
                DetailRow(
                    "Change (30d):",
                    formatPercentageChange(state.selectedCrypto.percentChange30d),
                    valueColor = if ((state.selectedCrypto.percentChange30d) >= 0) Green1 else Red1
                )
                DetailRow(
                    "Change (60d):",
                    formatPercentageChange(state.selectedCrypto.percentChange60d),
                    valueColor = if ((state.selectedCrypto.percentChange60d) >= 0) Green1 else Red1
                )
                DetailRow(
                    "Change (90d):",
                    formatPercentageChange(state.selectedCrypto.percentChange90d),
                    valueColor = if ((state.selectedCrypto.percentChange90d) >= 0) Green1 else Red1
                )
                DetailRow(
                    "Circulating Supply:",
                    formatLargeNumber(state.selectedCrypto.circulatingSupply)
                )
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Crypto not found!")
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String, valueColor: Color = Color.Unspecified) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
    }
}