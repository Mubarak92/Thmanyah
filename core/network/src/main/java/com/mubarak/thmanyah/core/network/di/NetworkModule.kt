package com.mubarak.thmanyah.core.network.di

import com.mubarak.thmanyah.core.network.BuildConfig
import com.mubarak.thmanyah.core.network.handler.NetworkConfig
import com.mubarak.thmanyah.core.network.handler.NetworkHandler
import com.mubarak.thmanyah.core.network.interceptor.HeaderInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideNetworkConfig(): NetworkConfig = NetworkConfig(
        baseUrl = "https://api-v2-b2sit6oh3a-uc.a.run.app/",
        isDebug = BuildConfig.DEBUG
    )

    @Provides
    @Singleton
    fun provideNetworkHandler(config: NetworkConfig): NetworkHandler = NetworkHandler.instance
        .addInterceptor(HeaderInterceptor())
        .setup(config)
}
