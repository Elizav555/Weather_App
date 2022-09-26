package com.example.weatherApp.di

import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class ApiIntercept

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MetricsIntercept
