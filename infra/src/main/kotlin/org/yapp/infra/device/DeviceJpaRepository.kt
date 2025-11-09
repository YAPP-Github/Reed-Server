package org.yapp.infra.device

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.yapp.infra.device.entity.DeviceEntity
import java.util.UUID

interface DeviceJpaRepository : JpaRepository<DeviceEntity, UUID> {
    fun findByDeviceId(deviceId: String): DeviceEntity?
    fun findByUserId(userId: UUID): List<DeviceEntity>

    @Modifying
    @Query("DELETE FROM DeviceEntity d WHERE d.fcmToken IN :tokens")
    fun deleteByFcmTokenIn(tokens: List<String>)
}
