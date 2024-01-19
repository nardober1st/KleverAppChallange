package com.oechslerbernardo.kleverappchallange

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.oechslerbernardo.kleverappchallange.presentation.details.CryptoDetailScreen
import com.oechslerbernardo.kleverappchallange.presentation.details.DetailsEvent
import com.oechslerbernardo.kleverappchallange.presentation.details.DetailsViewModel
import com.oechslerbernardo.kleverappchallange.presentation.main.MainEvent
import com.oechslerbernardo.kleverappchallange.presentation.main.MainScreen
import com.oechslerbernardo.kleverappchallange.presentation.main.MainViewModel
import com.oechslerbernardo.kleverappchallange.presentation.watchlist.WatchList
import com.oechslerbernardo.kleverappchallange.presentation.watchlist.WatchListEvent
import com.oechslerbernardo.kleverappchallange.presentation.watchlist.WatchListViewModel
import com.oechslerbernardo.kleverappchallange.ui.theme.KleverAppChallangeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KleverAppChallangeTheme {
                val navController = rememberNavController()
                val mainViewModel: MainViewModel = viewModel()
                NavHost(
                    navController = navController, startDestination = "watchListScreen"
                ) {
                    composable("watchListScreen") {
                        val viewModel: WatchListViewModel = hiltViewModel()
                        val state = viewModel.state
                        LaunchedEffect(Unit) {
                            viewModel.watchListChannelEvent.collect { event ->
                                when (event) {
                                    is WatchListEvent.OnAddCryptoNavigate -> {
                                        navController.navigate("mainScreen")
                                    }

                                    is WatchListEvent.OnCryptoClicked -> {
                                        navController.navigate("cryptoDetailScreen/${event.cryptoId}/true")
                                    }

                                    else -> {}
                                }
                            }
                        }
                        WatchList(
                            onEvent = viewModel::onEvent, state = state

                        )
                    }
                    composable("mainScreen") {
                        val state = mainViewModel.state

                        LaunchedEffect(key1 = Unit) {
                            mainViewModel.mainChannelEvent.collect { event ->
                                when (event) {
                                    is MainEvent.OnNavigateBack -> {
                                        navController.popBackStack()
                                    }

                                    is MainEvent.OnCryptoClicked -> {
                                        navController.navigate("cryptoDetailScreen/${event.cryptoId}/false")
                                    }

                                    else -> {}
                                }
                            }
                        }
                        MainScreen(state = state, onEvent = mainViewModel::onEvent)
                    }
                    composable(
                        route = "cryptoDetailScreen/{cryptoId}/{isFromDb}",
                        arguments = listOf(
                            navArgument("cryptoId") { type = NavType.IntType },
                            navArgument("isFromDb") { type = NavType.BoolType }
                        )
                    ) {
                        val detailsViewModel: DetailsViewModel = hiltViewModel()
                        val state = detailsViewModel.state

                        LaunchedEffect(key1 = Unit) {
                            detailsViewModel.detailsChannelEvent.collect { event ->
                                when (event) {
                                    is DetailsEvent.OnNavigateBack -> {
                                        navController.popBackStack()
                                    }

                                    is DetailsEvent.OnDeleteCrypto -> {
                                        navController.popBackStack()
                                    }

                                    is DetailsEvent.OnAddCrypto -> {
                                        navController.popBackStack()
                                    }
                                }
                            }
                        }
                        CryptoDetailScreen(
                            state = state, onEvent = detailsViewModel::onEvent
                        )
                    }
                }
            }
        }
    }
}