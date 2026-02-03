package com.mubarak.thmanyah.core.network.handler

data class NetworkConfig(
    val baseUrl: String,
    val isDebug: Boolean = false,
    val connectTimeoutSeconds: Long = 30,
    val readTimeoutSeconds: Long = 30
)
