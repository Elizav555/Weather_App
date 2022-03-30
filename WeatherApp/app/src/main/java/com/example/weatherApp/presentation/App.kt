package com.example.weatherApp.presentation

import android.app.Application
import com.example.weatherApp.di.*

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .dataModule(DataModule())
            .build()

        mainComponent = DaggerMainComponent.builder()
            .appComponent(appComponent)
            .domainModule(DomainModule())
            .build()
    }

    companion object {
        lateinit var appComponent: AppComponent
        lateinit var mainComponent: MainComponent
    }
}
