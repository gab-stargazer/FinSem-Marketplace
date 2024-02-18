package com.lelestacia.finsem_market.ui.screen.signup

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lelestacia.finsem_market.ui.theme.FinalSemesterMarketplaceTheme
import com.lelestacia.finsem_market.util.Email
import com.lelestacia.finsem_market.util.Name
import com.lelestacia.finsem_market.util.NavigateTo
import com.lelestacia.finsem_market.util.Password
import com.lelestacia.finsem_market.util.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    state: RegisterScreenState,
    onEvent: (RegisterScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val emailFocusRequester = remember {
        FocusRequester()
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Column {
            OutlinedTextField(
                singleLine = true,
                readOnly = state.isLoading,
                value = state.name.value,
                onValueChange = { newName ->
                    onEvent(RegisterScreenEvent.OnNameChanged(newName))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null
                    )
                },
                placeholder = {
                    Text(text = "Please insert Username")
                },
                label = {
                    Text(text = "Username")
                },
                isError = state.nameErrorMessage != null,
                supportingText = {
                    state.nameErrorMessage?.let { resID ->
                        Text(
                            text = stringResource(id = resID),
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.Red
                            )
                        )
                    }
                },
                keyboardActions = KeyboardActions(
                    onNext = {
                        emailFocusRequester.requestFocus()
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    autoCorrect = false,
                    capitalization = KeyboardCapitalization.Words,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },
                content = {
                    OutlinedTextField(
                        singleLine = true,
                        readOnly = true,
                        value = state.role.name.lowercase().capitalize(),
                        onValueChange = {},
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null
                            )
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expanded
                            )
                        },
                        label = {
                            Text(text = "Role")
                        },
                        supportingText = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        content = {
                            UserRole.entries.forEach {
                                DropdownMenuItem(
                                    text = {
                                        Text(text = it.name.lowercase().capitalize())
                                    },
                                    onClick = {
                                        onEvent(RegisterScreenEvent.OnRoleChanged(it))
                                        expanded = !expanded
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    )
                }
            )

            OutlinedTextField(
                singleLine = true,
                readOnly = state.isLoading,
                value = state.email.value,
                onValueChange = { newEmail ->
                    onEvent(RegisterScreenEvent.OnEmailChanged(newEmail))
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
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    autoCorrect = false,
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(emailFocusRequester)
                    .animateContentSize()
            )

            OutlinedTextField(
                singleLine = true,
                readOnly = state.isLoading,
                value = state.password.value,
                onValueChange = { newPassword ->
                    onEvent(RegisterScreenEvent.OnPasswordChanged(newPassword))
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
                            onEvent(RegisterScreenEvent.OnPasswordVisibilityChanged)
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
                keyboardActions = KeyboardActions(
                    onGo = {
                        focusManager.clearFocus()
                        onEvent(RegisterScreenEvent.OnRegister)
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    autoCorrect = false,
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Go
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
            )
        }

        AnimatedContent(
            targetState = state.isLoading,
            label = "Loading Animation"
        ) { isLoading ->
            when (isLoading) {
                true -> {
                    CircularProgressIndicator()
                }
                false -> {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = {
                                focusManager.clearFocus()
                                onEvent(RegisterScreenEvent.OnRegister)
                            }
                        ) {
                            Text(
                                text = "Sign Up",
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
                                text = "Already have an account?",
                                style = MaterialTheme.typography.bodySmall,
                            )
                            Text(
                                text = "Login Now",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Blue
                                ),
                                textDecoration = TextDecoration.Underline,
                                modifier = Modifier.clickable {
                                    onEvent(
                                        RegisterScreenEvent.OnNavigate(
                                            NavigateTo(
                                                route = "",
                                                navOptions = null,
                                                type = NavigateTo.NavType.POP
                                            )
                                        )
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

data class RegisterScreenState(
    val name: Name = Name(),
    val isLoading: Boolean = false,
    val nameErrorMessage: Int? = null,
    val role: UserRole = UserRole.Buyer,
    val email: Email = Email(),
    @StringRes val emailErrorMessage: Int? = null,
    val password: Password = Password(),
    val isPasswordVisible: Boolean = false,
    @StringRes val passwordErrorMessage: Int? = null,
    val registerEventMessage: String? = null,
    val navigateTo: NavigateTo? = null
)

sealed class RegisterScreenEvent {
    data class OnNameChanged(val name: String) : RegisterScreenEvent()
    data class OnRoleChanged(val role: UserRole) : RegisterScreenEvent()
    data class OnEmailChanged(val email: String) : RegisterScreenEvent()
    data class OnPasswordChanged(val password: String) : RegisterScreenEvent()
    data class OnNavigate(val navigation: NavigateTo?) : RegisterScreenEvent()
    data object OnPasswordVisibilityChanged : RegisterScreenEvent()
    data object OnRegister : RegisterScreenEvent()
    data object OnRegisterEventMessageHandled : RegisterScreenEvent()
}

@Preview
@Composable
fun PreviewRegisterScreen() {
    FinalSemesterMarketplaceTheme {
        var state by remember {
            mutableStateOf(RegisterScreenState())
        }

        RegisterScreen(
            state = state,
            onEvent = {}
        )
    }
}