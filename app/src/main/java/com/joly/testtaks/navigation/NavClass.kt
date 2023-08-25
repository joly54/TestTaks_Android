package com.joly.testtaks.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.joly.testtaks.screen.DisplayRepos
import com.joly.testtaks.screen.HomeScreen
import com.joly.testtaks.viewModel.AppViewModel

class NavClass {
    @Composable
    fun Navigation(
            navController: NavHostController,
            viewModel: AppViewModel
    ) {
        NavHost(navController = navController, startDestination = Routes.HomeScreen) {
            composable(Routes.HomeScreen) { HomeScreen().Screen(viewModel, navController)}
            composable(Routes.DisplayRepos + "/{login}") {
                val login = it.arguments?.getString("login") ?: ""
                DisplayRepos().Screen(login, viewModel)
            }
        }
    }
}