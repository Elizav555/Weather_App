package com.example.weatherApp.presentation

import android.app.Application
import com.example.weatherApp.di.components.AppComponent
import com.example.weatherApp.di.components.DaggerAppComponent
import com.example.weatherApp.di.components.DaggerMainComponent
import com.example.weatherApp.di.components.MainComponent
import com.example.weatherApp.di.modules.AppModule
import com.example.weatherApp.di.modules.DataModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .dataModule(DataModule())
            .build()

        mainComponent = DaggerMainComponent.builder()
            .appComponent(appComponent)
            .build()
    }

    companion object {
        lateinit var appComponent: AppComponent
        lateinit var mainComponent: MainComponent
    }
}
