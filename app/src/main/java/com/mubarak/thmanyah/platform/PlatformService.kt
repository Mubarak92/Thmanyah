package com.mubarak.thmanyah.platform

interface PlatformService {
    val serviceType: ServiceType
    fun initialize()
    fun getPushToken(callback: (String?) -> Unit)
}

enum class ServiceType { GMS, HMS }