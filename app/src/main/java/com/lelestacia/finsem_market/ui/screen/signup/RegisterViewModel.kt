package com.lelestacia.finsem_market.ui.screen.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.navOptions
import arrow.core.Either
import com.lelestacia.finsem_market.data.model.UserRegistrationDTO
import com.lelestacia.finsem_market.domain.usecases.RegisterUseCases
import com.lelestacia.finsem_market.util.Email
import com.lelestacia.finsem_market.util.Name
import com.lelestacia.finsem_market.util.NavigateTo
import com.lelestacia.finsem_market.util.NavigateTo.NavType
import com.lelestacia.finsem_market.util.Password
import com.lelestacia.finsem_market.util.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUseCases: RegisterUseCases
) : ViewModel() {

    private val _registerState: MutableStateFlow<RegisterScreenState> =
        MutableStateFlow(RegisterScreenState())
    val registerState: StateFlow<RegisterScreenState> =
        _registerState.asStateFlow()

    fun onEvent(event: RegisterScreenEvent) = viewModelScope.launch {
        when (event) {
            is RegisterScreenEvent.OnNameChanged -> {
                _registerState.update { state ->
                    state.copy(
                        name = Name(event.name),
                        nameErrorMessage = null
                    )
                }
            }

            is RegisterScreenEvent.OnRoleChanged -> {
                _registerState.update { state ->
                    state.copy(
                        role = event.role
                    )
                }
            }

            is RegisterScreenEvent.OnEmailChanged -> {
                _registerState.update { state ->
                    state.copy(
                        email = Email(event.email),
                        emailErrorMessage = null
                    )
                }
            }

            is RegisterScreenEvent.OnPasswordChanged -> {
                _registerState.update { state ->
                    state.copy(
                        password = Password(event.password),
                        passwordErrorMessage = null
                    )
                }
            }

            is RegisterScreenEvent.OnNavigate -> {
                _registerState.update { state ->
                    state.copy(
                        navigateTo = event.navigation
                    )
                }
            }

            RegisterScreenEvent.OnPasswordVisibilityChanged -> {
                _registerState.update { state ->
                    state.copy(
                        isPasswordVisible = !state.isPasswordVisible
                    )
                }
            }

            RegisterScreenEvent.OnRegister -> register()

            RegisterScreenEvent.OnRegisterEventMessageHandled -> {
                _registerState.update { state ->
                    state.copy(
                        registerEventMessage = null
                    )
                }
            }
        }
    }

    private fun register() = viewModelScope.launch {
        val validationResult = mutableListOf<Either<Int, Unit>>()

        _registerState.update { state ->
            state.copy(
                isLoading = true
            )
        }

        val getState: RegisterScreenState = registerState.value
        validationResult.add(registerUseCases.validateName(getState.name))
        validationResult.add(registerUseCases.validateEmail(getState.email))
        validationResult.add(registerUseCases.validatePassword(getState.password))

        if (validationResult.any { it.isLeft() }) {
            _registerState.update { currentState ->
                currentState.copy(
                    nameErrorMessage = validationResult.first().leftOrNull(),
                    emailErrorMessage = validationResult[1].leftOrNull(),
                    passwordErrorMessage = validationResult.last().leftOrNull(),
                    isLoading = false
                )
            }

            return@launch
        }

        try {
            _registerState.update { state ->
                state.copy(
                    registerEventMessage = registerUseCases.register(
                        email = getState.email.value,
                        password = getState.password.value,
                        registrationModel = UserRegistrationDTO(
                            name = getState.name.value,
                            role = getState.role.ordinal,
                            email = getState.email.value,
                        )
                    ),
                    isLoading = false,
                    navigateTo = NavigateTo(
                        route = Screen.Main.route,
                        navOptions = navOptions {
                            popUpTo(Screen.Auth.SignUp.route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        },
                        type = NavType.NAVIGATE
                    )
                )
            }
        } catch (e: Exception) {
            _registerState.update { state ->
                state.copy(
                    registerEventMessage = e.message.orEmpty(),
                    isLoading = false
                )
            }
        }
    }
}