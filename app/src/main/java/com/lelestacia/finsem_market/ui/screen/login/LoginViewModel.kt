package com.lelestacia.finsem_market.ui.screen.login

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.navOptions
import arrow.core.Either
import com.lelestacia.finsem_market.domain.usecases.LoginUseCases
import com.lelestacia.finsem_market.util.Email
import com.lelestacia.finsem_market.util.NavigateTo
import com.lelestacia.finsem_market.util.NavigateTo.NavType
import com.lelestacia.finsem_market.util.Password
import com.lelestacia.finsem_market.util.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCases: LoginUseCases
) : ViewModel() {

    private val _loginState: MutableStateFlow<LoginScreenState> =
        MutableStateFlow(LoginScreenState())
    val loginState: StateFlow<LoginScreenState> =
        _loginState.asStateFlow()

    fun onEvent(event: LoginScreenEvent) = viewModelScope.launch {
        when (event) {
            is LoginScreenEvent.OnEmailChanged -> {
                _loginState.update { state ->
                    state.copy(
                        email = Email(event.email),
                        emailErrorMessage = null
                    )
                }
            }

            is LoginScreenEvent.OnPasswordChanged -> {
                _loginState.update { state ->
                    state.copy(
                        password = Password(event.password),
                        passwordErrorMessage = null
                    )
                }
            }

            is LoginScreenEvent.OnNavigate -> {
                _loginState.update { state ->
                    state.copy(
                        navigateTo = event.navigation
                    )
                }
            }

            LoginScreenEvent.OnPasswordVisibilityChanged -> {
                _loginState.update { state ->
                    state.copy(
                        isPasswordVisible = !state.isPasswordVisible
                    )
                }
            }

            LoginScreenEvent.OnLogin -> login()

            LoginScreenEvent.OnLoginEventMessageHandled -> {
                _loginState.update { state ->
                    state.copy(
                        loginEventMessage = null
                    )
                }
            }
        }
    }

    private fun login() = viewModelScope.launch {
        val validationResult = mutableListOf<Either<Int, Unit>>()

//        MutableStateFlow Update and get is causing issue here
//        [ISSUE]: ViewModelStore should be set before setGraph call. Need further
//        investigation about this
//
//        val getState = _loginState.updateAndGet { state ->
//            state.copy(
//                isLoading = true
//            )
//        }

        _loginState.update { state ->
            state.copy(
                isLoading = true
            )
        }

        val getState: LoginScreenState = loginState.value

        validationResult.add(loginUseCases.validateEmail(getState.email))
        validationResult.add(loginUseCases.validatePassword(getState.password))

        if (validationResult.any { result -> result.isLeft() }) {
            _loginState.update { currentState ->
                currentState.copy(
                    emailErrorMessage = validationResult.first().leftOrNull(),
                    passwordErrorMessage = validationResult.last().leftOrNull()
                )
            }
            return@launch
        }

        try {
            _loginState.update { state ->
                state.copy(
                    loginEventMessage = loginUseCases.login(state.email, state.password),
                    isLoading = false,
                    navigateTo = NavigateTo(
                        route = Screen.Main.route,
                        navOptions = navOptions {
                            popUpTo(Screen.Auth.Login.route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        },
                        type = NavType.NAVIGATE
                    )
                )
            }
        } catch (e: Exception) {
            _loginState.update { state ->
                state.copy(
                    loginEventMessage = e.message.orEmpty(),
                    isLoading = false
                )
            }
        }
    }

    init {
        try {
            _loginState.update { state ->
                state.copy(
                    loginEventMessage = loginUseCases.checkUser(),
                    navigateTo = NavigateTo(
                        route = Screen.Main.route,
                        navOptions = navOptions {
                            popUpTo(Screen.Auth.Login.route) {
                                inclusive = true
                            }
                        },
                        type = NavType.NAVIGATE
                    )
                )
            }
        } catch (e: Exception) {
            Log.w(TAG, "Local Authentication Failed: User haven't login previously")
        }
    }
}