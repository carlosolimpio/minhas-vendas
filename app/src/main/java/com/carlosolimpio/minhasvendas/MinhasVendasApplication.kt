package com.carlosolimpio.minhasvendas

import android.app.Application
import com.carlosolimpio.minhasvendas.data.di.dataModule
import com.carlosolimpio.minhasvendas.presentation.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MinhasVendasApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MinhasVendasApplication)
            modules(dataModule, presentationModule)
        }
    }
}
