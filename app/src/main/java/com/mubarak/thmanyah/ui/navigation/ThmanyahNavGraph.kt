package com.mubarak.thmanyah.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mubarak.thmanyah.ui.screens.home.HomeScreen
import com.mubarak.thmanyah.ui.screens.home.HomeViewModel


object Routes {
    const val HOME = "home"
    const val SEARCH = "search"
}

@Composable
fun ThmanyahNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            val viewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                viewModel = viewModel,
                onSearchClick = { navController.navigate(Routes.SEARCH) }
            )
        }

//        composable(Routes.SEARCH) {
//            val viewModel: SearchViewModel = hiltViewModel()
//            SearchScreen(
//                viewModel = viewModel,
//                onBackClick = { navController.popBackStack() }
//            )
//        }
    }
}