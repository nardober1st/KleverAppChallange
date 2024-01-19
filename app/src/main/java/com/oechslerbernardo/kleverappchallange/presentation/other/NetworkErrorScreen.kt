package com.oechslerbernardo.kleverappchallange.presentation.other

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.oechslerbernardo.kleverappchallange.presentation.components.AnimatedEmptyMessage

@Composable
fun NetworkErrorScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedEmptyMessage(text = "No Internet Connection!")
        Spacer(modifier = Modifier.height(2.dp))
        Icon(
            imageVector = Icons.Filled.WifiOff, contentDescription = null,
            modifier = Modifier.size(50.dp)
        )
    }
}