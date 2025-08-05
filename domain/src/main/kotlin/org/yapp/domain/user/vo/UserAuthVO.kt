package org.yapp.domain.user.vo

import org.yapp.domain.user.User
import org.yapp.globalutils.auth.Role

data class UserAuthVO(
    val id: User.Id,
    val role: Role,
    val appleRefreshToken: String?
) {
    companion object {
        fun newInstance(user: User): UserAuthVO {
            return UserAuthVO(
                id = user.id,
                role = user.role,
                appleRefreshToken = user.appleRefreshToken
            )
        }
    }
}
