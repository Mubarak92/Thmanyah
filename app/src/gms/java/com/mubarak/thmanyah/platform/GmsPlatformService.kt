package com.mubarak.thmanyah.platform

class GmsPlatformService : PlatformService {
    override val serviceType = ServiceType.GMS
    override fun initialize() { /* Firebase init */ }
    override fun getPushToken(callback: (String?) -> Unit) { callback(null) }
}
