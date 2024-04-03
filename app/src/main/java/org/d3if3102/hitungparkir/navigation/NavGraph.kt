package org.d3if3102.hitungparkir.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.d3if3102.hitungparkir.ui.screen.InfoScreen
import org.d3if3102.hitungparkir.ui.screen.MainScreen

@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            MainScreen(modifier = Modifier.fillMaxWidth(), navController)
        }
        composable(route = Screen.Info.route){
            InfoScreen(modifier = Modifier.fillMaxWidth(),navController)
        }
    }
}