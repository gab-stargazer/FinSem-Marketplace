package com.lelestacia.finsem_market.ui.screen.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lelestacia.finsem_market.ui.screen.explore.ExploreScreen
import com.lelestacia.finsem_market.ui.screen.explore.ExploreViewModel
import com.lelestacia.finsem_market.ui.screen.profile.ProfileScreen
import com.lelestacia.finsem_market.ui.screen.profile.ProfileScreenEvent
import com.lelestacia.finsem_market.ui.screen.profile.ProfileViewModel
import com.lelestacia.finsem_market.util.Screen
import kotlinx.coroutines.channels.consumeEach
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    navController: NavHostController,
    onShowSnackbar: (String) -> Unit,
    onLogout: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Main.Explore.route,
        modifier = Modifier
            .fillMaxSize()
    ) {
        composable(Screen.Main.Explore.route) {
            val vm = koinViewModel<ExploreViewModel>()
            val services by vm.services.collectAsState()

            ExploreScreen(
                services
            )

            LaunchedEffect(key1 = Unit) {
                vm.eventMessage.consumeEach {
                    onShowSnackbar(it)
                }
            }
        }

        composable(Screen.Main.Profile.route) {
            val vm = koinViewModel<ProfileViewModel>()
            val state by vm.profileState.collectAsState()
            ProfileScreen(
                state = state,
                onEvent = vm::onEvent
            )

            LaunchedEffect(key1 = state.isSessionDismissed) {
                if (state.isSessionDismissed) {
                    onLogout()
                }
            }

            LaunchedEffect(key1 = state.profileEventMessage) {
                state.profileEventMessage?.let(onShowSnackbar).also {
                    vm.onEvent(ProfileScreenEvent.OnProfileEventMessageHandled)
                }
            }
        }
    }
}