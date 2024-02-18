package com.lelestacia.finsem_market.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lelestacia.finsem_market.data.local.PreferenceManager
import com.lelestacia.finsem_market.data.repository.AuthRepositoryImpl
import com.lelestacia.finsem_market.data.repository.ServiceRepositoryImpl
import com.lelestacia.finsem_market.domain.repository.AuthRepository
import com.lelestacia.finsem_market.domain.repository.ServiceRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {

    single<FirebaseFirestore> {
        Firebase.firestore
    }

    single<FirebaseAuth> {
        Firebase.auth
    }

    singleOf(::PreferenceManager) {
        createdAtStart()
    }

    singleOf(::AuthRepositoryImpl) {
        bind<AuthRepository>()
        createdAtStart()
    }

    singleOf(::ServiceRepositoryImpl) {
        bind<ServiceRepository>()
        createdAtStart()
    }
}