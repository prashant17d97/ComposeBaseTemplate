package com.example.basetemplate.di

import com.example.basetemplate.network.APIsEndPoints
import com.example.basetemplate.repo.Repository
import com.example.basetemplate.repo.RepositoryImpl
import com.example.basetemplate.repo.uistatus.UiStatusInfoImpl
import com.example.basetemplate.repo.uistatus.UiStatusInfoOwner
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Dagger Hilt module for providing dependencies used in the app.
 *
 * This module provides various dependencies such as exception handling, base URL for API requests,
 * OkHttpClient, GsonConverterFactory, Retrofit, API service, UI status information owner,
 * and the repository implementation.
 */
@InstallIn(SingletonComponent::class)
@Module
class Module {

    /**
     * Provides a [CoroutineExceptionHandler] to handle exceptions in coroutines.
     *
     * @return A CoroutineExceptionHandler instance.
     */
    @Provides
    @Singleton
    fun exceptionHandler() = CoroutineExceptionHandler { _, t ->
        t.printStackTrace()

        CoroutineScope(Dispatchers.Main).launch {
            t.printStackTrace()
        }
    }

    /**
     * Provides the base URL for API requests.
     *
     * @return The base URL as a string.
     */
    @Provides
    fun provideBaseUrl() = "https://jsonplaceholder.typicode.com/"

    /**
     * Provides an OkHttpClient with configured settings, including read and connect timeouts
     * and a logging interceptor for HTTP requests.
     *
     * @return An OkHttpClient instance.
     */
    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .readTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    /**
     * Provides a GsonConverterFactory for Retrofit to serialize and deserialize JSON data.
     *
     * @return A GsonConverterFactory instance.
     */
    @Singleton
    @Provides
    fun gsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    /**
     * Provides a Retrofit instance for making network requests with the specified OkHttpClient,
     * GsonConverterFactory, and base URL.
     *
     * @param okHttpClient The OkHttpClient instance.
     * @param gsonConverterFactory The GsonConverterFactory instance.
     * @param BASE_URL The base URL for API requests.
     * @return A Retrofit instance.
     */
    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        BASE_URL: String
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .addCallAdapterFactory(CoroutineCallAdapterFactory()).build()

    /**
     * Provides an instance of the API service interface.
     *
     * @param retrofit The Retrofit instance.
     * @return An instance of the APIsEndPoints interface.
     */
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): APIsEndPoints =
        retrofit.create(APIsEndPoints::class.java)

    /**
     * Provides an instance of the UiStatusInfoOwner interface.
     *
     * @return An implementation of UiStatusInfoOwner, e.g., UiStatusInfoImpl.
     */
    @Provides
    @Singleton
    fun provideUiStatusInfoImpl(): UiStatusInfoOwner = UiStatusInfoImpl()

    /**
     * Provides an instance of the repository, which interacts with the API and manages UI status information.
     *
     * @param aPIsEndPoints An instance of the APIsEndPoints interface.
     * @param uiStatusInfoOwner An instance of UiStatusInfoOwner.
     * @return An implementation of the Repository interface, e.g., RepositoryImpl.
     */
    @Provides
    @Singleton
    fun provideRepository(
        aPIsEndPoints: APIsEndPoints,
        uiStatusInfoOwner: UiStatusInfoOwner
    ): Repository = RepositoryImpl(aPIsEndPoints, uiStatusInfoOwner)
}
