package org.yapp.domain.device.vo

import org.yapp.domain.device.Device
import java.util.UUID

data class DeviceVO private constructor(
    val id: UUID,
    val userId: UUID,
    val fcmToken: String
) {
    companion object {
        fun from(device: Device): DeviceVO {
            return DeviceVO(
                id = device.id.value,
                userId = device.userId.value,
                fcmToken = device.fcmToken
            )
        }
    }
}
