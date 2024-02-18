package com.lelestacia.finsem_market.domain.util

import android.util.Patterns
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.lelestacia.finsem_market.R

class EmailValidation {

    operator fun invoke(email: String): Either<Int, Unit> = when {
        email.isBlank() -> R.string.error_email_is_empty.left()

        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> R.string.error_email_is_invalid.left()

        else -> Unit.right()
    }
}