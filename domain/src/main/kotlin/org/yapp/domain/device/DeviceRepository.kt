package org.yapp.domain.device

import java.util.UUID

interface DeviceRepository {
    fun findByDeviceId(deviceId: String): Device?
    fun save(device: Device): Device
    fun findByUserId(userId: UUID): List<Device>
    fun deleteByTokens(tokens: List<String>)
}
