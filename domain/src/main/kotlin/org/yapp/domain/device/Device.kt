package org.yapp.domain.device

import org.yapp.domain.user.User
import org.yapp.globalutils.util.UuidGenerator
import java.time.LocalDateTime
import java.util.UUID

data class Device private constructor(
    val id: Id,
    val userId: User.Id,
    val deviceId: String,
    val fcmToken: String,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) {
    companion object {
        fun create(
            userId: UUID,
            deviceId: String,
            fcmToken: String
        ): Device {
            return Device(
                id = Id.newInstance(UuidGenerator.create()),
                userId = User.Id.newInstance(userId),
                deviceId = deviceId,
                fcmToken = fcmToken
            )
        }

        fun reconstruct(
            id: Id,
            userId: User.Id,
            deviceId: String,
            fcmToken: String,
            createdAt: LocalDateTime?,
            updatedAt: LocalDateTime?
        ): Device {
            return Device(
                id = id,
                userId = userId,
                deviceId = deviceId,
                fcmToken = fcmToken,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }

        @JvmInline
        value class Id(val value: UUID) {
            companion object {
                fun newInstance(value: UUID) = Id(value)
            }
        }
    }
}
