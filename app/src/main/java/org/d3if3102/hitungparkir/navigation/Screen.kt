package org.d3if3102.hitungparkir.navigation

import org.d3if3102.hitungparkir.ui.screen.KEY_ID_LOGKENDARAAN

sealed class Screen(val route: String) {
    data object Home : Screen("mainScreen")
    data object Info : Screen("infoScreen")
    data object List : Screen("listScreen")
    data object ListUpdate : Screen("listUpdateScreen/{$KEY_ID_LOGKENDARAAN}") {
        fun withId(id: Long) = "listUpdateScreen/$id"
    }
}