package org.yapp.infra.device.entity

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.yapp.domain.device.Device
import org.yapp.domain.user.User
import org.yapp.infra.common.BaseTimeEntity
import java.sql.Types
import java.util.UUID

@Entity
@Table(name = "device")
class DeviceEntity(
    @Id
    @JdbcTypeCode(Types.VARCHAR)
    @Column(length = 36, updatable = false, nullable = false)
    val id: UUID,

    @JdbcTypeCode(Types.VARCHAR)
    @Column(name = "user_id", length = 36, nullable = false)
    val userId: UUID,

    @Column(name = "device_id", nullable = false)
    var deviceId: String,

    @Column(name = "fcm_token", nullable = false)
    var fcmToken: String,
) : BaseTimeEntity() {

    fun toDomain(): Device {
        return Device.reconstruct(
            id = Device.Id.newInstance(this.id),
            userId = User.Id.newInstance(this.userId),
            deviceId = this.deviceId,
            fcmToken = this.fcmToken,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }

    companion object {
        fun fromDomain(device: Device): DeviceEntity {
            return DeviceEntity(
                id = device.id.value,
                userId = device.userId.value,
                deviceId = device.deviceId,
                fcmToken = device.fcmToken
            )
        }
    }
}
