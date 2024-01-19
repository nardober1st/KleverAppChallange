package com.oechslerbernardo.kleverappchallange.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.oechslerbernardo.kleverappchallange.domain.model.Crypto
import com.oechslerbernardo.kleverappchallange.ui.theme.Green1
import com.oechslerbernardo.kleverappchallange.ui.theme.Red1
import com.oechslerbernardo.kleverappchallange.util.formatChangeAmount
import com.oechslerbernardo.kleverappchallange.util.formatPercentageChange
import com.oechslerbernardo.kleverappchallange.util.formatPrice

@Composable
fun CryptoListItem(crypto: Crypto, onCryptoClick: (Int) -> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable { onCryptoClick(crypto.id) }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = crypto.symbol,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = crypto.name,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = formatPrice(crypto.price),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Row {
                        Text(
                            text = formatChangeAmount(crypto.price, crypto.percentChange24h),
                            color = if (crypto.percentChange24h >= 0) Green1 else Red1,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.padding(3.dp))
                        Text(
                            text = formatPercentageChange(crypto.percentChange24h),
                            color = if (crypto.percentChange24h >= 0) Green1 else Red1,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
        Divider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}