package com.mubarak.thmanyah.core.network.handler

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkHandler private constructor() {

    private lateinit var retrofit: Retrofit
    private lateinit var config: NetworkConfig
    private val interceptors = mutableListOf<Interceptor>()

    fun setup(config: NetworkConfig): NetworkHandler {
        this.config = config
        this.retrofit = buildRetrofit()
        return this
    }

    fun addInterceptor(interceptor: Interceptor): NetworkHandler {
        interceptors.add(interceptor)
        return this
    }

    fun <T> create(serviceClass: Class<T>): T {
        check(::retrofit.isInitialized) { "NetworkHandler not initialized. Call setup() first." }
        return retrofit.create(serviceClass)
    }

    inline fun <reified T> create(): T = create(T::class.java)

    private fun buildRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(config.baseUrl)
        .client(buildOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun buildOkHttpClient(): OkHttpClient = OkHttpClient.Builder().apply {
        connectTimeout(config.connectTimeoutSeconds, TimeUnit.SECONDS)
        readTimeout(config.readTimeoutSeconds, TimeUnit.SECONDS)
        interceptors.forEach { addInterceptor(it) }
        if (config.isDebug) {
            addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        }
    }.build()

    companion object {
        val instance: NetworkHandler by lazy { NetworkHandler() }
    }
}
