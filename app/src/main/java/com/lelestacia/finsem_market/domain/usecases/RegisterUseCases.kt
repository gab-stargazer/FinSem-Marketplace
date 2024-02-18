package com.lelestacia.finsem_market.domain.usecases

import arrow.core.Either
import com.lelestacia.finsem_market.data.model.UserRegistrationDTO
import com.lelestacia.finsem_market.domain.repository.AuthRepository
import com.lelestacia.finsem_market.domain.util.EmailValidation
import com.lelestacia.finsem_market.domain.util.NameValidation
import com.lelestacia.finsem_market.domain.util.PasswordValidation
import com.lelestacia.finsem_market.util.Email
import com.lelestacia.finsem_market.util.Name
import com.lelestacia.finsem_market.util.Password

class RegisterUseCases(
    private val nameValidation: NameValidation,
    private val emailValidation: EmailValidation,
    private val passwordValidation: PasswordValidation,
    private val repository: AuthRepository
) {

    fun validateName(name: Name): Either<Int, Unit> {
        return nameValidation(name.value)
    }

    fun validateEmail(email: Email): Either<Int, Unit> {
        return emailValidation(email.value)
    }

    fun validatePassword(password: Password): Either<Int, Unit> {
        return passwordValidation(password.value)
    }

    suspend fun register(email: String, password: String, registrationModel: UserRegistrationDTO): String {
        return repository.register(email, password, registrationModel)
    }
}