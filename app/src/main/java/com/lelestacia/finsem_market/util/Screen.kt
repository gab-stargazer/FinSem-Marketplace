package com.lelestacia.finsem_market.util

sealed class Screen(open val route: String) {

    data object Auth : Screen("auth") {
        data object Login : Screen("login")
        data object SignUp : Screen("signup")
    }

    data object Main : Screen("main") {
        data object Explore : Screen("explore")
        data object Profile : Screen("profile")
    }
}