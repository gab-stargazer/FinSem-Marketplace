package com.lelestacia.finsem_market.domain.util

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.lelestacia.finsem_market.R

class NameValidation {

    operator fun invoke(name: String): Either<Int, Unit> =
        when {
            name.isBlank() -> R.string.error_name_is_empty.left()
            else -> Unit.right()
        }
}