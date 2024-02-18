package com.lelestacia.finsem_market.domain.repository

import com.lelestacia.finsem_market.data.model.UserDto
import com.lelestacia.finsem_market.data.model.UserRegistrationDTO

interface AuthRepository {
    suspend fun login(email: String, password: String): String
    suspend fun register(
        email: String,
        password: String,
        registrationModel: UserRegistrationDTO
    ): String
    fun checkUser(): String
    fun getUser(): UserDto
    fun logout()
    suspend fun updateUser(userDto: UserDto): String
}