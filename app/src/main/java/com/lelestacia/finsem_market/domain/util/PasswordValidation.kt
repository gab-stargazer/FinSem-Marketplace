package com.lelestacia.finsem_market.domain.util

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.lelestacia.finsem_market.R

class PasswordValidation {

    operator fun invoke(password: String): Either<Int, Unit> = when {
        password.isBlank() -> R.string.error_password_is_empty.left()

        password.length < 8 -> R.string.error_password_less_than_required.left()

        else -> Unit.right()
    }
}