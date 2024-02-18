package com.lelestacia.finsem_market.di

import com.lelestacia.finsem_market.ui.screen.explore.ExploreViewModel
import com.lelestacia.finsem_market.ui.screen.login.LoginViewModel
import com.lelestacia.finsem_market.ui.screen.profile.ProfileViewModel
import com.lelestacia.finsem_market.ui.screen.signup.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::ExploreViewModel)
}