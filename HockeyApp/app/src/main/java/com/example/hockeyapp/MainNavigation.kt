package com.example.hockeyapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.composable
import com.example.hockeyapp.ui.screens.auth.LoginScreen
import com.example.hockeyapp.ui.screens.auth.RegisterScreen
import com.example.hockeyapp.ui.screens.players.PlayerScreen
import com.example.hockeyapp.ui.screens.teams.TeamListScreen

@Composable
fun MainNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        // Auth routes
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }

        // Team routes
        composable("team_list") { TeamListScreen(navController) }
        composable("team_registration") { TeamRegistrationScreen(navController) }

        // Player routes
        composable("player_list") { PlayerScreen(navController) }
        composable("player_registration") { PlayerRegistrationScreen(navController) }

        // Event routes
        composable("event_list") { EventScreen(navController) }
        composable("event_registration") { EventRegistrationScreen(navController) }

        // News routes
        composable("news_list") { NewsListScreen(navController) }
    }
}