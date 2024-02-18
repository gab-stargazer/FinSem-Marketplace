package com.lelestacia.finsem_market.ui.screen.profile

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lelestacia.finsem_market.R
import com.lelestacia.finsem_market.data.model.UserDto
import com.lelestacia.finsem_market.ui.theme.FinalSemesterMarketplaceTheme
import com.lelestacia.finsem_market.util.UserRole
import com.lelestacia.finsem_market.util.launchUrlIntent
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Brands
import compose.icons.fontawesomeicons.brands.Github
import compose.icons.fontawesomeicons.brands.Linkedin
import compose.icons.fontawesomeicons.brands.Whatsapp

@Composable
fun ProfileScreen(
    state: ProfileScreenState,
    onEvent: (ProfileScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                readOnly = true,
                value = state.user.name,
                onValueChange = {},
                label = {
                    Text(
                        text = "Name",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold
                        )
                    )
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Person, contentDescription = null)
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                readOnly = true,
                value = state.user.email,
                onValueChange = {},
                label = {
                    Text(
                        text = "Email",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold
                        )
                    )
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Email, contentDescription = null)
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                readOnly = true,
                value = UserRole.entries[state.user.role].name.lowercase().capitalize(),
                onValueChange = {},
                label = {
                    Text(
                        text = "Role",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold
                        )
                    )
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Info, contentDescription = null)
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )

            if (UserRole.entries[state.user.role] == UserRole.Seller) {
                OutlinedTextField(
                    readOnly = false,
                    value = state.user.githubUrl.orEmpty(),
                    onValueChange = { newGithubUrl ->
                        onEvent(ProfileScreenEvent.OnGithubUrlChanged(newGithubUrl))
                    },
                    placeholder = {
                        Text(text = "Please setup your Github Profile URL here")
                    },
                    label = {
                        Text(
                            text = "Github",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = FontAwesomeIcons.Brands.Github,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    trailingIcon = {
                        AnimatedVisibility(
                            visible = state.user.githubUrl.orEmpty().isNotBlank(),
                            enter = slideInHorizontally()
                        ) {
                            IconButton(
                                onClick = {
                                    try {
                                        context.launchUrlIntent(state.user.githubUrl.orEmpty())
                                    } catch (e: Exception) {
                                        onEvent(
                                            ProfileScreenEvent.OnSendEventMessage(
                                                context.getString(
                                                    R.string.error_invalid_profile_url
                                                )
                                            )
                                        )
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.OpenInNew,
                                    contentDescription = null
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    readOnly = false,
                    value = state.user.linkedinURL.orEmpty(),
                    onValueChange = { newLinkedinUrl ->
                        onEvent(ProfileScreenEvent.OnLinkedinUrlChanged(newLinkedinUrl))
                    },
                    placeholder = {
                        Text(text = "Please setup your Linkedin Profile URL here")
                    },
                    label = {
                        Text(
                            text = "Linkedin",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = FontAwesomeIcons.Brands.Linkedin,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    trailingIcon = {
                        AnimatedVisibility(
                            visible = state.user.linkedinURL.orEmpty().isNotBlank(),
                            enter = slideInHorizontally()
                        ) {
                            IconButton(
                                onClick = {
                                    try {
                                        context.launchUrlIntent(state.user.linkedinURL.orEmpty())
                                    } catch (e: Exception) {
                                        onEvent(
                                            ProfileScreenEvent.OnSendEventMessage(
                                                context.getString(
                                                    R.string.error_invalid_profile_url
                                                )
                                            )
                                        )
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.OpenInNew,
                                    contentDescription = null
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    readOnly = state.isLoading,
                    value = state.user.whatsappNumber.orEmpty(),
                    onValueChange = { newPhoneNumber ->
                        onEvent(ProfileScreenEvent.OnPhoneNumberChanged(newPhoneNumber))
                    },
                    placeholder = {
                        Text(text = "Please setup your Whatsapp Phone Number here")
                    },
                    label = {
                        Text(
                            text = "Whatsapp",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                    },
                    prefix = {
                        Text(
                            text = "+",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = FontAwesomeIcons.Brands.Whatsapp,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        AnimatedContent(
            targetState = state.isLoading,
            label = "Loading Indicator"
        ) { isLoading ->
            when (isLoading) {
                true -> Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CircularProgressIndicator()
                }

                false -> {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Button(
                            enabled = state.isDataChanged && !state.isLoading,
                            onClick = {
                                onEvent(ProfileScreenEvent.OnUpdateProfile)
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Update Profile",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        OutlinedButton(
                            onClick = {
                                onEvent(ProfileScreenEvent.OnLogout)
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                contentColor = Color.Red,
                                containerColor = Color.Transparent
                            ),
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Logout",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

data class ProfileScreenState(
    val user: UserDto = UserDto(),
    val isDataChanged: Boolean = false,
    val profileEventMessage: String? = null,
    val isLoading: Boolean = false,
    val isSessionDismissed: Boolean = false
)

sealed class ProfileScreenEvent {
    data class OnGithubUrlChanged(val githubUrl: String) : ProfileScreenEvent()
    data class OnLinkedinUrlChanged(val linkedinUrl: String) : ProfileScreenEvent()
    data class OnPhoneNumberChanged(val phoneNumber: String) : ProfileScreenEvent()
    data class OnSendEventMessage(val message: String) : ProfileScreenEvent()
    data object OnUpdateProfile : ProfileScreenEvent()
    data object OnProfileEventMessageHandled : ProfileScreenEvent()
    data object OnLogout : ProfileScreenEvent()
}

@Preview
@Composable
fun PreviewProfileScreen() {
    FinalSemesterMarketplaceTheme {
        ProfileScreen(
            state = ProfileScreenState(
                user = UserDto(
                    name = "Kamil Malik",
                    email = "km8003296@gmail.com",
                    role = 0,
                    githubUrl = "https://github.com/gab-stargazer",
                    linkedinURL = "https://www.linkedin.com/in/kamil-malik/",
                    whatsappNumber = "62822696969"
                )
            ),
            onEvent = {}
        )
    }
}