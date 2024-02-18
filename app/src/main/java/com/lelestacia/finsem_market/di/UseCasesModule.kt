package com.lelestacia.finsem_market.di

import com.lelestacia.finsem_market.domain.usecases.ExploreUseCases
import com.lelestacia.finsem_market.domain.usecases.LoginUseCases
import com.lelestacia.finsem_market.domain.usecases.ProfileUseCases
import com.lelestacia.finsem_market.domain.usecases.RegisterUseCases
import com.lelestacia.finsem_market.domain.util.EmailValidation
import com.lelestacia.finsem_market.domain.util.NameValidation
import com.lelestacia.finsem_market.domain.util.PasswordValidation
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val useCasesModule = module {
    factoryOf(::EmailValidation)
    factoryOf(::PasswordValidation)
    factoryOf(::NameValidation)
    factoryOf(::LoginUseCases)
    factoryOf(::RegisterUseCases)
    factoryOf(::ProfileUseCases)
    factoryOf(::ExploreUseCases)
}