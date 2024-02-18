package com.lelestacia.finsem_market.ui.screen.profile

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lelestacia.finsem_market.data.model.UserDto
import com.lelestacia.finsem_market.domain.usecases.ProfileUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val useCases: ProfileUseCases
) : ViewModel() {

    private val _profileState: MutableStateFlow<ProfileScreenState> =
        MutableStateFlow(ProfileScreenState())
    val profileState: StateFlow<ProfileScreenState> =
        _profileState.asStateFlow()

    private val _savedUser: MutableStateFlow<UserDto> =
        MutableStateFlow(UserDto())

    fun onEvent(event: ProfileScreenEvent) = viewModelScope.launch {
        when (event) {
            is ProfileScreenEvent.OnGithubUrlChanged -> {
                _profileState.update { state ->
                    state.copy(
                        user = state.user.copy(
                            githubUrl = event.githubUrl
                        )
                    )
                }

                checkForDifferences()
            }

            is ProfileScreenEvent.OnLinkedinUrlChanged -> {
                _profileState.update { state ->
                    state.copy(
                        user = state.user.copy(
                            linkedinURL = event.linkedinUrl
                        )
                    )
                }

                checkForDifferences()
            }

            is ProfileScreenEvent.OnPhoneNumberChanged -> {
                _profileState.update { state ->
                    state.copy(
                        user = state.user.copy(
                            whatsappNumber = event.phoneNumber
                        )
                    )
                }

                checkForDifferences()
            }

            is ProfileScreenEvent.OnSendEventMessage -> {
                _profileState.update { state ->
                    state.copy(
                        profileEventMessage = event.message
                    )
                }
            }

            ProfileScreenEvent.OnLogout -> logout()

            ProfileScreenEvent.OnUpdateProfile -> updateProfile()

            ProfileScreenEvent.OnProfileEventMessageHandled -> {
                _profileState.update { state ->
                    state.copy(
                        profileEventMessage = null
                    )
                }
            }

        }
    }

    private fun updateProfile() = viewModelScope.launch {
        val currentState = _profileState.updateAndGet { state ->
            state.copy(
                isLoading = true,
                user = state.user.copy(
                    githubUrl =
                    if (state.user.githubUrl.isNullOrBlank()) {
                        null
                    } else {
                        state.user.githubUrl
                    },
                    linkedinURL =
                    if (state.user.linkedinURL.isNullOrBlank()) {
                        null
                    } else {
                        state.user.linkedinURL
                    },
                    whatsappNumber =
                    if (state.user.whatsappNumber.isNullOrBlank()) {
                        null
                    } else {
                        state.user.whatsappNumber
                    }
                )
            )
        }

        Log.d(TAG, "updateProfile: $currentState")

        try {
            _profileState.update { state ->
                state.copy(
                    profileEventMessage = useCases.updateProfile(currentState.user),
                    isLoading = false,
                    isDataChanged = false
                )
            }
        } catch (e: Exception) {
            _profileState.update { state ->
                state.copy(
                    profileEventMessage = e.message.orEmpty(),
                    isLoading = false
                )
            }
        }
        resetProfileData()
    }

    private fun logout() = viewModelScope.launch {
        useCases.logout()
        _profileState.update { state ->
            state.copy(
                isSessionDismissed = true
            )
        }
    }

    private fun checkForDifferences() {
        _profileState.update { state ->
            state.copy(
                isDataChanged = state.user != _savedUser.value
            )
        }
    }

    private fun resetProfileData() = viewModelScope.launch {
        try {
            val user = useCases.getUser()
            val modifiedUser = user.copy(
                githubUrl = if (user.githubUrl.isNullOrBlank()) "" else user.githubUrl,
                linkedinURL = if (user.linkedinURL.isNullOrBlank()) "" else user.linkedinURL,
                whatsappNumber = if (user.whatsappNumber.isNullOrBlank()) "" else user.whatsappNumber
            )

            _profileState.update { state ->
                state.copy(
                    user = modifiedUser
                )
            }

            _savedUser.update { _ ->
                modifiedUser
            }
        } catch (e: Exception) {
            Log.e(TAG, "Get User: Login data not found")
            logout()
        }
    }

    init {
        resetProfileData()
    }
}