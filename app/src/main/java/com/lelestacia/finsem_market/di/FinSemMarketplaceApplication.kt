package com.lelestacia.finsem_market.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class FinSemMarketplaceApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@FinSemMarketplaceApplication)
            modules(listOf(viewModelModule, useCasesModule, repositoryModule))
            createEagerInstances()
        }
    }
}