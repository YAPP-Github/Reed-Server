package org.yapp.domain.user.vo

import org.yapp.domain.user.User
import org.yapp.globalutils.auth.Role
import java.util.UUID

data class UserIdentity(
    val id: UUID,
    val role: Role
) {
    init {
        requireNotNull(id) { "User ID must not be null." }
        requireNotNull(role) { "User role must not be null." }
    }

    companion object {
        fun newInstance(user: User?): UserIdentity {
            requireNotNull(user) { "User must not be null." }
            return UserIdentity(
                id = user.id,
                role = user.role
            )
        }
    }
}
