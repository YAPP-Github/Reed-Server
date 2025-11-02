package org.yapp.infra.device.entity

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.yapp.domain.device.Device
import org.yapp.infra.common.BaseTimeEntity
import org.yapp.infra.user.entity.UserEntity
import java.sql.Types
import java.util.UUID

@Entity
@Table(name = "device")
class DeviceEntity(
    @Id
    @JdbcTypeCode(Types.VARCHAR)
    @Column(length = 36, updatable = false, nullable = false)
    val id: UUID,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserEntity,

    @Column(name = "device_id", nullable = false)
    var deviceId: String,

    @Column(name = "fcm_token", nullable = false)
    var fcmToken: String,
) : BaseTimeEntity() {

    companion object {
        fun fromDomain(device: Device): DeviceEntity {
            return DeviceEntity(
                id = device.id.value,
                user = UserEntity.fromDomain(device.user),
                deviceId = device.deviceId,
                fcmToken = device.fcmToken
            )
        }
    }

    fun toDomain(): Device {
        return Device.reconstruct(
            id = Device.Id.newInstance(this.id),
            user = this.user.toDomain(),
            deviceId = this.deviceId,
            fcmToken = this.fcmToken,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }
}
