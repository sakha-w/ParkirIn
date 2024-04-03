package org.d3if3102.hitungparkir.navigation

sealed class Screen(val route: String) {
    data object Home: Screen("mainScreen")
    data object Info: Screen("infoScreen")
}