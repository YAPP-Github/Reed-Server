package org.yapp.infra.device

import org.springframework.data.jpa.repository.JpaRepository
import org.yapp.infra.device.entity.DeviceEntity
import java.util.UUID

interface DeviceJpaRepository : JpaRepository<DeviceEntity, UUID> {
    fun findByDeviceId(deviceId: String): DeviceEntity?
    fun findByUserId(userId: UUID): List<DeviceEntity>
}
