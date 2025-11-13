package org.yapp.domain.device

import org.yapp.domain.device.vo.DeviceVO
import org.yapp.globalutils.annotation.DomainService
import java.util.UUID

@DomainService
class DeviceDomainService(
    private val deviceRepository: DeviceRepository
) {
    fun findOrCreateDevice(userId: UUID, deviceId: String, fcmToken: String) {
        val device = deviceRepository.findByDeviceId(deviceId)
        if (device == null) {
            val newDevice = Device.create(userId, deviceId, fcmToken)
            deviceRepository.save(newDevice)
        }
    }

    fun findDevicesByUserId(userId: UUID): List<DeviceVO> {
        return deviceRepository.findByUserId(userId)
            .map { DeviceVO.from(it) }
    }

    fun findDeviceByFcmToken(fcmToken: String): DeviceVO? {
        return deviceRepository.findByFcmToken(fcmToken)
            ?.let { DeviceVO.from(it) }
    }

    fun removeDevicesByTokens(tokens: List<String>) {
        if (tokens.isNotEmpty()) {
            deviceRepository.deleteByTokens(tokens)
        }
    }
}
