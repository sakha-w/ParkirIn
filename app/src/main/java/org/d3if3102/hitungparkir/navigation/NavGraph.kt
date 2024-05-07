package org.d3if3102.hitungparkir.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.d3if3102.hitungparkir.ui.screen.InfoScreen
import org.d3if3102.hitungparkir.ui.screen.KEY_ID_LOGKENDARAAN
import org.d3if3102.hitungparkir.ui.screen.ListScreen
import org.d3if3102.hitungparkir.ui.screen.ListScreenContent
import org.d3if3102.hitungparkir.ui.screen.ListUpdateScreen
import org.d3if3102.hitungparkir.ui.screen.MainScreen
import org.d3if3102.hitungparkir.ui.screen.ScreenContent
import org.d3if3102.hitungparkir.ui.screen.UpdateContent

@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            MainScreen(modifier = Modifier.fillMaxWidth(), navController)
        }
        composable(route = Screen.Info.route) {
            InfoScreen(modifier = Modifier.fillMaxWidth(), navController)
        }
        composable(route = Screen.List.route) {
            ListScreen(modifier = Modifier.fillMaxWidth(), navController = navController)
        }
        composable(route = Screen.ListUpdate.route, arguments = listOf(
            navArgument(KEY_ID_LOGKENDARAAN) {
                type = NavType.LongType
            }
        )
        ) { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getLong(KEY_ID_LOGKENDARAAN)
            ListUpdateScreen(modifier = Modifier.fillMaxWidth(), navController, id)
        }
    }
}