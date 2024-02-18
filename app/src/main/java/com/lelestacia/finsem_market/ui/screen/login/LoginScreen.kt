package com.lelestacia.finsem_market.ui.screen.login

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lelestacia.finsem_market.ui.theme.FinalSemesterMarketplaceTheme
import com.lelestacia.finsem_market.util.Email
import com.lelestacia.finsem_market.util.NavigateTo
import com.lelestacia.finsem_market.util.Password
import com.lelestacia.finsem_market.util.Screen

@Composable
fun LoginScreen(
    state: LoginScreenState,
    onEvent: (LoginScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Column {
            OutlinedTextField(
                readOnly = state.isLoading,
                value = state.email.value,
                singleLine = true,
                onValueChange = { newEmail ->
                    onEvent(LoginScreenEvent.OnEmailChanged(newEmail))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null
                    )
                },
                placeholder = {
                    Text(text = "Please insert a valid Email")
                },
                label = {
                    Text(text = "Email")
                },
                isError = state.emailErrorMessage != null,
                supportingText = {
                    state.emailErrorMessage?.let { resID ->
                        Text(
                            text = stringResource(id = resID),
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.Red
                            )
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    autoCorrect = false,
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
            )

            OutlinedTextField(
                readOnly = state.isLoading,
                value = state.password.value,
                singleLine = true,
                onValueChange = { newPassword ->
                    onEvent(LoginScreenEvent.OnPasswordChanged(newPassword))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null
                    )
                },
                placeholder = {
                    Text(text = "Please insert your Password")
                },
                label = {
                    Text(text = "Password")
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            onEvent(LoginScreenEvent.OnPasswordVisibilityChanged)
                        }
                    ) {
                        AnimatedContent(
                            targetState = state.isPasswordVisible,
                            label = "Password Visibility Animation"
                        ) { isVisible ->
                            when (isVisible) {
                                true -> {
                                    Icon(
                                        imageVector = Icons.Default.VisibilityOff,
                                        contentDescription = null
                                    )
                                }

                                false -> {
                                    Icon(
                                        imageVector = Icons.Default.Visibility,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                },
                isError = state.passwordErrorMessage != null,
                supportingText = {
                    state.passwordErrorMessage?.let { resID ->
                        Text(
                            text = stringResource(id = resID),
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.Red
                            )
                        )
                    }
                },
                visualTransformation =
                when (state.isPasswordVisible) {
                    true -> VisualTransformation.None
                    false -> PasswordVisualTransformation('*')
                },
                keyboardOptions = KeyboardOptions(
                    autoCorrect = false,
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Go
                ),
                keyboardActions = KeyboardActions(
                    onGo = {
                        focusManager.clearFocus()
                        onEvent(LoginScreenEvent.OnLogin)
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            AnimatedContent(
                targetState = state.isLoading,
                label = "Login Loading Indicator"
            ) { isLoading ->
                when (isLoading) {
                    true -> CircularProgressIndicator()
                    false -> {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                enabled = !state.isLoading,
                                onClick = { onEvent(LoginScreenEvent.OnLogin) }
                            ) {
                                Text(
                                    text = "Login",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Don't have an account?",
                                    style = MaterialTheme.typography.bodySmall,
                                )
                                Text(
                                    text = "Sign Up Now",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Blue
                                    ),
                                    textDecoration = TextDecoration.Underline,
                                    modifier = Modifier.clickable {
                                        focusManager.clearFocus()
                                        val navigation = NavigateTo(
                                            route = Screen.Auth.SignUp.route,
                                            navOptions = null,
                                            type = NavigateTo.NavType.NAVIGATE
                                        )
                                        onEvent(LoginScreenEvent.OnNavigate(navigation))
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

data class LoginScreenState(
    val email: Email = Email(),
    @StringRes val emailErrorMessage: Int? = null,
    val password: Password = Password(),
    val isPasswordVisible: Boolean = false,
    @StringRes val passwordErrorMessage: Int? = null,
    val loginEventMessage: String? = null,
    val isLoading: Boolean = false,
    val navigateTo: NavigateTo? = null
)

sealed class LoginScreenEvent {
    data class OnEmailChanged(val email: String) : LoginScreenEvent()
    data class OnPasswordChanged(val password: String) : LoginScreenEvent()
    data class OnNavigate(val navigation: NavigateTo?) : LoginScreenEvent()
    data object OnPasswordVisibilityChanged : LoginScreenEvent()
    data object OnLogin : LoginScreenEvent()
    data object OnLoginEventMessageHandled : LoginScreenEvent()
}

@Preview
@Composable
fun PreviewLoginScreen() {
    FinalSemesterMarketplaceTheme {
        var state: LoginScreenState by remember {
            mutableStateOf(
                LoginScreenState()
            )
        }
        LoginScreen(
            state = state,
            onEvent = {
                when (it) {
                    is LoginScreenEvent.OnEmailChanged -> {
                        state = state.copy(
                            email = Email(it.email)
                        )
                    }

                    is LoginScreenEvent.OnPasswordChanged -> {
                        state = state.copy(
                            password = Password(it.password)
                        )
                    }

                    is LoginScreenEvent.OnNavigate -> {
                        state = state.copy(
                            navigateTo = it.navigation
                        )
                    }

                    LoginScreenEvent.OnPasswordVisibilityChanged -> {
                        state = state.copy(
                            isPasswordVisible = !state.isPasswordVisible
                        )
                    }

                    LoginScreenEvent.OnLogin -> {

                    }

                    LoginScreenEvent.OnLoginEventMessageHandled -> {

                    }
                }
            }
        )
    }
}