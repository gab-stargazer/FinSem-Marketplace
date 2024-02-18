package com.lelestacia.finsem_market.util

import androidx.navigation.NavOptions

data class NavigateTo(
    val route: String,
    val navOptions: NavOptions?,
    val type: NavType
) {
    enum class NavType {
        NAVIGATE, POP
    }
}