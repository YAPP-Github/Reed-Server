package org.yapp.domain.device

import org.yapp.domain.user.User
import org.yapp.globalutils.annotation.DomainService

@DomainService
class DeviceDomainService(
    private val deviceRepository: DeviceRepository
) {
    fun findOrCreateDevice(user: User, deviceId: String, fcmToken: String): Device {
        val device = deviceRepository.findByDeviceId(deviceId)
        return if (device != null) {
            device
        } else {
            val newDevice = Device.create(user, deviceId, fcmToken)
            deviceRepository.save(newDevice)
        }
    }
}
