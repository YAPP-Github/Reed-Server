package org.yapp.domain.user.vo

import org.yapp.domain.user.User
import org.yapp.globalutils.auth.Role

data class UserIdentityVO(
    val id: User.Id,
    val role: Role
) {
    companion object {
        fun newInstance(user: User): UserIdentityVO {
            return UserIdentityVO(
                id = user.id,
                role = user.role
            )
        }
    }
}
