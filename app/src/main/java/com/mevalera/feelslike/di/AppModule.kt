package com.mevalera.feelslike.di

import android.content.Context
import com.mevalera.feelslike.BuildConfig
import com.mevalera.feelslike.data.repository.CityPreferencesRepository
import com.mevalera.feelslike.data.repository.CityPreferencesRepositoryImpl
import com.mevalera.feelslike.data.repository.WeatherRepository
import com.mevalera.feelslike.data.repository.WeatherRepositoryImpl
import com.mevalera.feelslike.data.source.local.DataStoreService
import com.mevalera.feelslike.data.source.remote.WeatherService
import com.mevalera.feelslike.domain.usecases.GetCityWeatherUseCase
import com.mevalera.feelslike.domain.usecases.GetCityWeatherUseCaseImpl
import com.mevalera.feelslike.domain.usecases.GetSelectedCityUseCase
import com.mevalera.feelslike.domain.usecases.GetSelectedCityUseCaseImpl
import com.mevalera.feelslike.domain.usecases.SaveSelectedCityUseCase
import com.mevalera.feelslike.domain.usecases.SaveSelectedCityUseCaseImpl
import com.mevalera.feelslike.domain.usecases.SearchCitiesUseCase
import com.mevalera.feelslike.domain.usecases.SearchCitiesUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideDispatchers(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideDataStoreService(
        @ApplicationContext context: Context,
    ): DataStoreService = DataStoreService(context)

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val loggingInterceptor =
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

        val client =
            OkHttpClient
                .Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build()

        return Retrofit
            .Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherService(retrofit: Retrofit): WeatherService = retrofit.create(WeatherService::class.java)

    @Provides
    @Singleton
    fun provideWeatherRepository(repository: WeatherRepositoryImpl): WeatherRepository = repository

    @Provides
    @Singleton
    fun provideGetCityWeatherUseCase(weatherRepository: WeatherRepository): GetCityWeatherUseCase =
        GetCityWeatherUseCaseImpl(weatherRepository)

    @Provides
    @Singleton
    fun provideSearchCitiesUseCase(weatherRepository: WeatherRepository): SearchCitiesUseCase = SearchCitiesUseCaseImpl(weatherRepository)

    @Provides
    @Singleton
    fun provideCityPreferencesRepository(dataStoreService: DataStoreService): CityPreferencesRepository =
        CityPreferencesRepositoryImpl(dataStoreService)

    @Provides
    @Singleton
    fun provideSaveSelectedCityUseCase(cityPreferencesRepository: CityPreferencesRepository): SaveSelectedCityUseCase =
        SaveSelectedCityUseCaseImpl(cityPreferencesRepository)

    @Provides
    @Singleton
    fun provideGetSelectedCityUseCase(cityPreferencesRepository: CityPreferencesRepository): GetSelectedCityUseCase =
        GetSelectedCityUseCaseImpl(cityPreferencesRepository)
}
