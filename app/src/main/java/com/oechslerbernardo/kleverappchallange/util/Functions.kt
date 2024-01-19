package com.oechslerbernardo.kleverappchallange.util

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

fun formatPrice(price: Double): String {
    return when {
        price >= 1 -> {
            val format = NumberFormat.getCurrencyInstance(Locale.US)
            format.format(price)
        }

        price > 0 -> {
            val decimalFormat = DecimalFormat("$0.##########")
            decimalFormat.format(price)
        }

        else -> {
            val decimalFormat = DecimalFormat("0.####E0")
            decimalFormat.format(price)
        }
    }
}

fun formatChangeAmount(price: Double, percentChange24h: Double): String {
    val changeAmount = price * percentChange24h / 100.0

    return when {
        kotlin.math.abs(changeAmount) >= 1 -> {
            val format = NumberFormat.getCurrencyInstance(Locale.US)
            format.format(changeAmount)
        }
        kotlin.math.abs(changeAmount) > 0 -> {
            val decimalFormat = DecimalFormat("$0.##########")
            decimalFormat.format(changeAmount)
        }
        else -> {
            val decimalFormat = DecimalFormat("0.####E0")
            decimalFormat.format(changeAmount)
        }
    }
}

fun formatPercentageChange(percentChange24h: Double): String {
    return if (percentChange24h > 0) {
        String.format("+%.2f%%", percentChange24h)
    } else {
        String.format("%.2f%%", percentChange24h)
    }
}

fun formatLargeNumber(number: Double): String {
    // Format the number as needed (e.g., with commas)
    return NumberFormat.getNumberInstance().format(number)
}