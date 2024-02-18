package com.lelestacia.finsem_market.domain.usecases

import arrow.core.Either
import com.lelestacia.finsem_market.domain.repository.AuthRepository
import com.lelestacia.finsem_market.domain.util.EmailValidation
import com.lelestacia.finsem_market.domain.util.PasswordValidation
import com.lelestacia.finsem_market.util.Email
import com.lelestacia.finsem_market.util.Password

class LoginUseCases(
    private val emailValidation: EmailValidation,
    private val passwordValidation: PasswordValidation,
    private val repository: AuthRepository
) {

    fun validateEmail(email: Email): Either<Int, Unit> {
        return emailValidation(email.value)
    }

    fun validatePassword(password: Password): Either<Int, Unit> {
        return passwordValidation(password.value)
    }

    suspend fun login(email: Email, password: Password): String {
        return repository.login(email.value, password.value)
    }

    fun checkUser(): String {
        return repository.checkUser()
    }
}