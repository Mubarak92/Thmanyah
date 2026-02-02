package com.mubarak.thmanyah.di

import com.mubarak.thmanyah.data.remote.ThmanyahApiService
import com.mubarak.thmanyah.data.repository.ThmanyahRepository
import com.mubarak.thmanyah.data.repository.ThmanyahRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(client: OkHttpClient): ThmanyahApiService {
        // Base URL is a placeholder — actual URLs are passed via @Url in each call
        return Retrofit.Builder()
            .baseUrl("")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ThmanyahApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(api: ThmanyahApiService): ThmanyahRepository {
        return ThmanyahRepositoryImpl(api)
    }
}