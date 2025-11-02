package org.yapp.domain.device

import org.yapp.domain.user.User
import org.yapp.globalutils.util.UuidGenerator
import java.time.LocalDateTime
import java.util.UUID

data class Device private constructor(
    val id: Id,
    val user: User,
    val deviceId: String,
    val fcmToken: String,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) {
    @JvmInline
    value class Id(val value: UUID) {
        companion object {
            fun newInstance(value: UUID) = Id(value)
        }
    }

    companion object {
        fun create(
            user: User,
            deviceId: String,
            fcmToken: String
        ): Device {
            return Device(
                id = Id.newInstance(UuidGenerator.create()),
                user = user,
                deviceId = deviceId,
                fcmToken = fcmToken
            )
        }

        fun reconstruct(
            id: Id,
            user: User,
            deviceId: String,
            fcmToken: String,
            createdAt: LocalDateTime?,
            updatedAt: LocalDateTime?
        ): Device {
            return Device(
                id = id,
                user = user,
                deviceId = deviceId,
                fcmToken = fcmToken,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
    }
}
