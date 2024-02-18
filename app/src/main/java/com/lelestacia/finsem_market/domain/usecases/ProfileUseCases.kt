package com.lelestacia.finsem_market.domain.usecases

import com.lelestacia.finsem_market.data.model.UserDto
import com.lelestacia.finsem_market.domain.repository.AuthRepository

class ProfileUseCases(
    private val authRepository: AuthRepository
) {

    fun getUser(): UserDto {
        return authRepository.getUser()
    }

    fun logout() {
        authRepository.logout()
    }

    suspend fun updateProfile(userDto: UserDto): String {
        return authRepository.updateUser(userDto)
    }
}