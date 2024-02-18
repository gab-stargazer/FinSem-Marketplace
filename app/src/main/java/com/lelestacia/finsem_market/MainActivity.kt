package com.lelestacia.finsem_market

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.lelestacia.finsem_market.ui.component.bottomBarItems
import com.lelestacia.finsem_market.ui.screen.login.LoginScreen
import com.lelestacia.finsem_market.ui.screen.login.LoginScreenEvent
import com.lelestacia.finsem_market.ui.screen.login.LoginScreenState
import com.lelestacia.finsem_market.ui.screen.login.LoginViewModel
import com.lelestacia.finsem_market.ui.screen.main.MainScreen
import com.lelestacia.finsem_market.ui.screen.signup.RegisterScreen
import com.lelestacia.finsem_market.ui.screen.signup.RegisterScreenEvent
import com.lelestacia.finsem_market.ui.screen.signup.RegisterScreenState
import com.lelestacia.finsem_market.ui.screen.signup.RegisterViewModel
import com.lelestacia.finsem_market.ui.theme.FinalSemesterMarketplaceTheme
import com.lelestacia.finsem_market.util.NavigateTo
import com.lelestacia.finsem_market.util.Screen
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinalSemesterMarketplaceTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val childNavController = rememberNavController()

                    val scope = rememberCoroutineScope()
                    val snackbarHostState by remember {
                        mutableStateOf(SnackbarHostState())
                    }

                    val backStackEntry by navController.currentBackStackEntryAsState()
                    val childBackStackEntry by childNavController.currentBackStackEntryAsState()

                    Scaffold(
                        bottomBar = {
                            AnimatedVisibility(
                                visible = backStackEntry?.destination?.route.equals(Screen.Main.route),
                                enter = slideInVertically {
                                    it / 2
                                }
                            ) {
                                NavigationBar {
                                    bottomBarItems.forEach { item ->
                                        NavigationBarItem(
                                            selected = childBackStackEntry?.destination?.route.equals(
                                                item.screen.route
                                            ),
                                            onClick = {
                                                childNavController.navigate(item.screen.route) {
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
                        },
                        snackbarHost = {
                            SnackbarHost(
                                hostState = snackbarHostState
                            )
                        }
                    ) { _ ->
                        NavHost(
                            navController = navController,
                            startDestination = Screen.Auth.route
                        ) {
                            navigation(
                                route = Screen.Auth.route,
                                startDestination = Screen.Auth.Login.route
                            ) {
                                composable(
                                    route = Screen.Auth.Login.route,
                                    exitTransition = {
                                        fadeOut()
                                    }
                                ) {
                                    val vm: LoginViewModel = koinViewModel()
                                    val state: LoginScreenState by vm.loginState.collectAsState()
                                    LoginScreen(state = state, onEvent = vm::onEvent)
                                    LaunchedEffect(key1 = state.navigateTo) {
                                        state.navigateTo?.let { nav ->
                                            when (nav.type) {
                                                NavigateTo.NavType.NAVIGATE -> {
                                                    navController.navigate(
                                                        route = nav.route,
                                                        navOptions = nav.navOptions
                                                    )
                                                }

                                                NavigateTo.NavType.POP -> navController.popBackStack()
                                            }

                                            vm.onEvent(LoginScreenEvent.OnNavigate(null))
                                        }
                                    }

                                    LaunchedEffect(key1 = state.loginEventMessage) {
                                        state.loginEventMessage?.let {
                                            scope.launch {
                                                snackbarHostState.showSnackbar(it)
                                                vm.onEvent(LoginScreenEvent.OnLoginEventMessageHandled)
                                            }
                                        }
                                    }
                                }

                                composable(
                                    route = Screen.Auth.SignUp.route,
                                    enterTransition = {
                                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up)
                                    }
                                ) {
                                    val vm: RegisterViewModel = koinViewModel()
                                    val state: RegisterScreenState by vm.registerState.collectAsState()
                                    RegisterScreen(
                                        state = state,
                                        onEvent = vm::onEvent
                                    )

                                    LaunchedEffect(key1 = state.navigateTo) {
                                        state.navigateTo?.let { nav ->
                                            when (nav.type) {
                                                NavigateTo.NavType.NAVIGATE -> {
                                                    navController.navigate(
                                                        route = nav.route,
                                                        navOptions = nav.navOptions
                                                    )
                                                }

                                                NavigateTo.NavType.POP -> navController.popBackStack()
                                            }

                                            vm.onEvent(RegisterScreenEvent.OnNavigate(null))
                                        }
                                    }

                                    LaunchedEffect(key1 = state.registerEventMessage) {
                                        state.registerEventMessage?.let {
                                            scope.launch {
                                                snackbarHostState.showSnackbar(it)
                                            }
                                            vm.onEvent(RegisterScreenEvent.OnRegisterEventMessageHandled)
                                        }
                                    }
                                }
                            }

                            composable(Screen.Main.route) {
                                MainScreen(
                                    navController = childNavController,
                                    onShowSnackbar = { message ->
                                        scope.launch {
                                            snackbarHostState.showSnackbar(message)
                                        }
                                    },
                                    onLogout = {
                                        navController.navigate(
                                            route = Screen.Auth.route,
                                            navOptions = navOptions {
                                                popUpTo(Screen.Main.route) {
                                                    inclusive = true
                                                }
                                            }
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FinalSemesterMarketplaceTheme {
        Greeting("Android")
    }
}