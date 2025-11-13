package org.yapp.infra.device

import org.springframework.stereotype.Repository
import org.yapp.domain.device.Device
import org.yapp.domain.device.DeviceRepository
import org.yapp.infra.device.entity.DeviceEntity
import java.util.UUID

@Repository
class DeviceRepositoryImpl(
    private val deviceJpaRepository: DeviceJpaRepository
) : DeviceRepository {
    override fun findByDeviceId(deviceId: String): Device? {
        return deviceJpaRepository.findByDeviceId(deviceId)?.toDomain()
    }

    override fun findByFcmToken(fcmToken: String): Device? {
        return deviceJpaRepository.findByFcmToken(fcmToken)?.toDomain()
    }

    override fun save(device: Device): Device {
        return deviceJpaRepository.save(DeviceEntity.fromDomain(device)).toDomain()
    }

    override fun findByUserId(userId: UUID): List<Device> {
        return deviceJpaRepository.findByUserId(userId).map { it.toDomain() }
    }

    override fun deleteByTokens(tokens: List<String>) {
        deviceJpaRepository.deleteByFcmTokenIn(tokens)
    }
}
