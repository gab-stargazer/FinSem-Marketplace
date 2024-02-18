package com.lelestacia.finsem_market.ui.screen.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lelestacia.finsem_market.ui.component.bottomBarItems
import com.lelestacia.finsem_market.ui.screen.explore.ExploreScreen
import com.lelestacia.finsem_market.ui.screen.explore.ExploreViewModel
import com.lelestacia.finsem_market.ui.screen.profile.ProfileScreen
import com.lelestacia.finsem_market.ui.screen.profile.ProfileScreenEvent
import com.lelestacia.finsem_market.ui.screen.profile.ProfileViewModel
import com.lelestacia.finsem_market.util.Screen
import kotlinx.coroutines.channels.consumeEach
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(
    onLogout: () -> Unit
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val snackbarHostState by remember {
        mutableStateOf(SnackbarHostState())
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        bottomBar = {
            NavigationBar {
                bottomBarItems.forEach { item ->
                    NavigationBarItem(
                        selected = backStackEntry?.destination?.route.equals(
                            item.screen.route
                        ),
                        onClick = {
                            navController.navigate(item.screen.route) {
                                launchSingleTop = true
                                popUpTo(Screen.Main.Explore.route) {
                                    saveState = true
                                }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(
                                text = stringResource(id = item.title),
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        },
                        alwaysShowLabel = false
                    )
                }
            }
        }
    ) { paddingValue ->
        NavHost(
            navController = navController,
            startDestination = Screen.Main.Explore.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {
            composable(Screen.Main.Explore.route) {
                val vm = koinViewModel<ExploreViewModel>()
                val services by vm.services.collectAsState()

                ExploreScreen(
                    services
                )

                LaunchedEffect(key1 = Unit) {
                    vm.eventMessage.consumeEach {
                        snackbarHostState.showSnackbar(it)
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
                    state.profileEventMessage?.let {
                        snackbarHostState.showSnackbar(it)
                        vm.onEvent(ProfileScreenEvent.OnProfileEventMessageHandled)
                    }
                }
            }
        }
    }
}