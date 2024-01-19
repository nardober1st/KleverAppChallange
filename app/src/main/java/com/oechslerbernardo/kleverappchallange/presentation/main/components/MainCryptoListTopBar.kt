package com.oechslerbernardo.kleverappchallange.presentation.main.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoListTopBar(
    onBackPress: () -> Unit,
    onRefresh: () -> Unit,
    onSort: () -> Unit,
    showMenu: MutableState<Boolean>,
    onSortOptionSelected: (String, String) -> Unit
) {
    TopAppBar(
        title = { Text(text = "CryptosList") },
        navigationIcon = {
            IconButton(onClick = onBackPress) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go Back")
            }
        },
        actions = {
            IconButton(onClick = onRefresh) {
                Icon(imageVector = Icons.Filled.Refresh, contentDescription = "Refresh")
            }
            IconButton(onClick = onSort) {
                Icon(imageVector = Icons.Filled.Sort, contentDescription = "Sort")
            }
            DropdownMenu(
                expanded = showMenu.value,
                onDismissRequest = { showMenu.value = false }) {
                DropdownMenuItem(text = {
                    Text(text = "Name: A to Z")
                }, onClick = {
                    onSortOptionSelected("name", "asc")
                    showMenu.value = false
                })
                DropdownMenuItem(text = {
                    Text(text = "Name: Z to A")
                }, onClick = {
                    onSortOptionSelected("name", "desc")
                    showMenu.value = false
                })
                DropdownMenuItem(text = {
                    Text(text = "Market cap: High to Low")
                }, onClick = {
                    onSortOptionSelected("market_cap", "desc")
                    showMenu.value = false
                })
                DropdownMenuItem(text = {
                    Text(text = "Market cap: Low to High")
                }, onClick = {
                    onSortOptionSelected("market_cap", "asc")
                    showMenu.value = false
                })
                DropdownMenuItem(text = {
                    Text(text = "Price: High to Low")
                }, onClick = {
                    onSortOptionSelected("price", "desc")
                    showMenu.value = false
                })
            }
        }
    )
}