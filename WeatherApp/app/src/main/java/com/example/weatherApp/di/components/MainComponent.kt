package com.example.weatherApp.di.components

import com.example.weatherApp.di.ActivityScope
import com.example.weatherApp.di.modules.DomainModule
import com.example.weatherApp.presentation.MainActivity
import com.example.weatherApp.presentation.fragments.CityFragment
import com.example.weatherApp.presentation.fragments.HomeFragment
import dagger.Component

@ActivityScope
@Component(dependencies = [AppComponent::class], modules = [DomainModule::class])
interface MainComponent {

    fun inject(activity: MainActivity)

    fun inject(homeFragment: HomeFragment)

    fun inject(cityFragment: CityFragment)
}