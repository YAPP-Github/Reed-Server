package org.yapp.domain.device

import org.yapp.globalutils.annotation.DomainService
import java.util.UUID

@DomainService
class DeviceDomainService(
    private val deviceRepository: DeviceRepository
) {
    fun findOrCreateDevice(userId: UUID, deviceId: String, fcmToken: String): Device {
        val device = deviceRepository.findByDeviceId(deviceId)
        return if (device != null) {
            device
        } else {
            val newDevice = Device.create(userId, deviceId, fcmToken)
            deviceRepository.save(newDevice)
        }
    }
}
