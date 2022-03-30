package com.example.weatherApp.di

import androidx.viewbinding.BuildConfig
import com.example.weatherApp.data.WeatherApi
import com.example.weatherApp.data.WeatherRepositoryImpl
import com.example.weatherApp.data.mapper.CityMapper
import com.example.weatherApp.domain.WeatherRepository
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun provideWeatherRepository(
        api: WeatherApi,
        cityMapper: CityMapper
    ): WeatherRepository {
        return WeatherRepositoryImpl(
            api = api,
            cityMapper = cityMapper
        )
    }

    @Provides
    @Singleton
    fun provideWeatherApi(retrofit: Retrofit): WeatherApi = retrofit
        .create(WeatherApi::class.java)

    @Provides
    @Singleton
    fun provideRetrofit(
        okhttp: OkHttpClient,
        converterFactory: GsonConverterFactory,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okhttp)
        .addConverterFactory(converterFactory)
        .build()

    @Provides
    @Singleton
    @Named(TAG_APIKEY)
    fun provideApiKeyInterceptor() = Interceptor { chain ->
        val original = chain.request()
        val newURL = original.url.newBuilder()
            .addQueryParameter(QUERY_API_KEY, API_KEY)
            .build()

        chain.proceed(
            original.newBuilder()
                .url(newURL)
                .build()
        )
    }

    @Provides
    @Singleton
    @Named(TAG_METRICS)
    fun provideMetricsInterceptor() = Interceptor { chain ->
        val original = chain.request()
        val newURL = original.url.newBuilder()
            .addQueryParameter(QUERY_UNITS, UNITS)
            .build()
        chain.proceed(
            original.newBuilder()
                .url(newURL)
                .build()
        )
    }

    @Provides
    @Singleton
    fun provideClient(
        @Named(TAG_APIKEY) apiKeyInterceptor: Interceptor,
        @Named(TAG_METRICS) metricsInterceptor: Interceptor
    ) = OkHttpClient.Builder()
        .addInterceptor(apiKeyInterceptor)
        .addInterceptor(metricsInterceptor)
        .also {
            if (BuildConfig.DEBUG) {
                it.addInterceptor(
                    HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)
                )
            }
        }
        .build()

    @Provides
    @Singleton
    fun provideConvertFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideCityMapper(): CityMapper = CityMapper()

    companion object {
        private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
        private const val API_KEY = "aa36cfdd9ceba87b783295438fe008a6"
        private const val QUERY_API_KEY = "appid"
        private const val UNITS = "metric"
        private const val QUERY_UNITS = "units"

        private const val TAG_METRICS = "Metrics"
        private const val TAG_APIKEY = "Api key"
    }
}
