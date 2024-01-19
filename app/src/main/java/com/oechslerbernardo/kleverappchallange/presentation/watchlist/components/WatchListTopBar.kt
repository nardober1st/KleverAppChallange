package com.oechslerbernardo.kleverappchallange.presentation.watchlist.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchListTopAppBar(
    onAddCryptoClick: () -> Unit,
    onDeleteAllClick: () -> Unit,
    onRefreshClick: () -> Unit,
    onSortAscClick: () -> Unit,
    onSortDescClick: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var sortingMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text(text = "Watchlist") },
        actions = {
            IconButton(onClick = { sortingMenu = true }) {
                Icon(imageVector = Icons.Filled.Sort, contentDescription = "Sort")
            }
            DropdownMenu(
                expanded = sortingMenu,
                onDismissRequest = { sortingMenu = false }) {
                DropdownMenuItem(
                    text = { Text(text = "Name: A to Z") },
                    onClick = {
                        sortingMenu = false
                        onSortAscClick()
                    }
                )
                DropdownMenuItem(
                    text = { Text(text = "Name: Z to A") },
                    onClick = {
                        sortingMenu = false
                        onSortDescClick()
                    }
                )
            }
            IconButton(onClick = { showMenu = true }) {
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "More")
            }
            DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                DropdownMenuItem(
                    text = { Text(text = "Add symbol") },
                    onClick = {
                        showMenu = false
                        onAddCryptoClick()
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
                    }
                )
                DropdownMenuItem(
                    text = { Text(text = "Delete All") },
                    onClick = {
                        onDeleteAllClick()
                        showMenu = false
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.DeleteForever,
                            contentDescription = "Delete All"
                        )
                    }
                )
                DropdownMenuItem(
                    text = { Text(text = "Refresh data") },
                    onClick = {
                        onRefreshClick()
                        showMenu = false
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Filled.Refresh, contentDescription = "Refresh")
                    }
                )
            }
        }
    )
}