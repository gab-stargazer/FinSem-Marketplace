package com.lelestacia.finsem_market.ui.component

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.lelestacia.finsem_market.R
import com.lelestacia.finsem_market.util.Screen

data class BottomBarItem(
    @StringRes val title: Int,
    val icon: ImageVector,
    val screen: Screen
)

val bottomBarItems: List<BottomBarItem> = listOf(
    BottomBarItem(
        R.string.menu_explore,
        Icons.Default.Explore,
        Screen.Main.Explore
    ),
    BottomBarItem(
        R.string.menu_profile,
        Icons.Default.Person,
        Screen.Main.Profile
    ),
)
