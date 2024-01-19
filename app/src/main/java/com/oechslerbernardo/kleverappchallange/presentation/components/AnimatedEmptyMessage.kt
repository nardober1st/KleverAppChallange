package com.oechslerbernardo.kleverappchallange.presentation.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AnimatedEmptyMessage(text: String) {
    var sizeState by remember { mutableStateOf(10.sp) }

    val textSize by animateFloatAsState(
        targetValue = sizeState.value,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        ), label = ""
    )

    Text(
        text = text,
        modifier = Modifier.padding(16.dp),
        fontSize = textSize.sp
    )

    LaunchedEffect(Unit) {
        sizeState = 20.sp
    }
}