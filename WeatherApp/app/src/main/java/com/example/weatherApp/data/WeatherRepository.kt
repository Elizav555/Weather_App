package com.example.weatherApp.data

import androidx.viewbinding.BuildConfig
import com.example.weatherApp.data.response.City
import com.example.weatherApp.data.response.Coord
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
private const val API_KEY = "aa36cfdd9ceba87b783295438fe008a6"
private const val QUERY_API_KEY = "appid"
private const val UNITS = "metric"
private const val QUERY_UNITS = "units"

class WeatherRepository {

    private val apiKeyInterceptor = Interceptor { chain ->
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

    private val metricsInterceptor = Interceptor { chain ->
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

    private val okhttp: OkHttpClient by lazy {
        OkHttpClient.Builder()
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
    }

    private val api: WeatherApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okhttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }

    suspend fun getWeather(cityName: String): City = api.getWeather(cityName)

    /*suspend fun getWeather(cityName: String): City? {
            return try {
            api.getWeather(cityName)
            } catch (e: Exception) {
                return null
            }
        }*/

    suspend fun getWeatherNearLocation(coordinates: Coord): List<City> =
        api.getWeatherNearLocation(coordinates.lat.toString(), coordinates.lon.toString()).list

    suspend fun getWeather(cityId: Int): City = api.getWeather(cityId)
}
