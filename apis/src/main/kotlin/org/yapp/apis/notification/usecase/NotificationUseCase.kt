package org.yapp.apis.notification.usecase

import org.yapp.domain.notification.NotificationDomainService
import org.yapp.domain.user.UserDomainService
import org.yapp.globalutils.annotation.UseCase
import java.util.UUID

@UseCase
class NotificationUseCase(
    private val notificationDomainService: NotificationDomainService,
    private val userDomainService: UserDomainService
) {
    fun registerFcmToken(userId: UUID, token: String) {
        val user = userDomainService.findUserById(userId)
        notificationDomainService.registerToken(user, token)
    }
}
