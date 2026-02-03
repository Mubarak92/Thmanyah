package com.mubarak.thmanyah.platform

class HmsPlatformService : PlatformService {
    override val serviceType = ServiceType.HMS
    override fun initialize() {  }
    override fun getPushToken(callback: (String?) -> Unit) { callback(null) }
}
