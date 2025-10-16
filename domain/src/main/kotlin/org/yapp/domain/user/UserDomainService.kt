package org.yapp.domain.user

import org.yapp.domain.user.exception.UserErrorCode
import org.yapp.domain.user.exception.UserNotFoundException
import org.yapp.domain.user.vo.UserAuthVO
import org.yapp.domain.user.vo.UserIdentityVO
import org.yapp.domain.user.vo.UserProfileVO
import org.yapp.domain.user.vo.WithdrawTargetUserVO
import org.yapp.domain.userbook.UserBookRepository
import org.yapp.domain.readingrecord.ReadingRecordRepository
import org.yapp.globalutils.annotation.DomainService

import java.time.LocalDateTime
import java.util.*

@DomainService
class UserDomainService(
    private val userRepository: UserRepository,
    private val userBookRepository: UserBookRepository,
    private val readingRecordRepository: ReadingRecordRepository
) {
    fun findUserProfileById(id: UUID): UserProfileVO {
        val user = userRepository.findById(id) ?: throw UserNotFoundException(UserErrorCode.USER_NOT_FOUND)
        return UserProfileVO.newInstance(user)
    }

    fun findUserIdentityById(id: UUID): UserIdentityVO {
        val user = userRepository.findById(id) ?: throw UserNotFoundException(UserErrorCode.USER_NOT_FOUND)
        return UserIdentityVO.newInstance(user)
    }

    fun findWithdrawUserById(id: UUID): WithdrawTargetUserVO {
        val user = userRepository.findById(id) ?: throw UserNotFoundException(UserErrorCode.USER_NOT_FOUND)
        return WithdrawTargetUserVO.newInstance(user)
    }

    fun findUserByProviderTypeAndProviderId(providerType: ProviderType, providerId: String): UserAuthVO? {
        return userRepository.findByProviderTypeAndProviderId(providerType, providerId)
            ?.let { UserAuthVO.newInstance(it) }
    }

    fun findUserByProviderTypeAndProviderIdIncludingDeleted(
        providerType: ProviderType,
        providerId: String
    ): UserAuthVO? {
        return userRepository.findByProviderTypeAndProviderIdIncludingDeleted(providerType, providerId)
            ?.let { UserAuthVO.newInstance(it) }
    }

    fun existsActiveUserByIdAndDeletedAtIsNull(id: UUID): Boolean {
        return userRepository.existsById(id)
    }

    fun existsActiveUserByEmailAndDeletedAtIsNull(email: String): Boolean {
        return userRepository.existsByEmail(email)
    }

    fun findUserById(id: UUID): User {
        return userRepository.findById(id) ?: throw UserNotFoundException(UserErrorCode.USER_NOT_FOUND)
    }


    fun createUser(
        email: String,
        nickname: String,
        profileImageUrl: String?,
        providerType: ProviderType,
        providerId: String
    ): UserAuthVO {
        val user = User.create(
            email = email,
            nickname = nickname,
            profileImageUrl = profileImageUrl,
            providerType = providerType,
            providerId = providerId
        )
        val savedUser = userRepository.save(user)
        return UserAuthVO.newInstance(savedUser)
    }

    fun restoreDeletedUser(userId: UUID): UserAuthVO {
        val deletedUser = userRepository.findByIdIncludingDeleted(userId)
            ?: throw UserNotFoundException(UserErrorCode.USER_NOT_FOUND)

        val restoredUser = userRepository.save(deletedUser.restore())
        return UserAuthVO.newInstance(restoredUser)
    }

    fun updateTermsAgreement(userId: UUID, termsAgreed: Boolean): UserProfileVO {
        val user = userRepository.findById(userId)
            ?: throw UserNotFoundException(UserErrorCode.USER_NOT_FOUND)

        val updatedUser = userRepository.save(user.updateTermsAgreement(termsAgreed))
        return UserProfileVO.newInstance(updatedUser)
    }

    fun updateAppleRefreshToken(userId: UUID, refreshToken: String): UserAuthVO {
        val user = userRepository.findById(userId)
            ?: throw UserNotFoundException(UserErrorCode.USER_NOT_FOUND)

        val updatedUser = userRepository.save(user.updateAppleRefreshToken(refreshToken))

        return UserAuthVO.newInstance(updatedUser)
    }

    fun deleteUser(userId: UUID) {
        val user = userRepository.findById(userId)
            ?: throw UserNotFoundException(UserErrorCode.USER_NOT_FOUND)

        userRepository.deleteById(user.id.value)
    }

    /**
     * Updates the user's lastActivity value if they have registered or recorded a book in the last 7 days.
     * If not, the lastActivity value remains unchanged (keeps the login date).
     * 
     * @param userId The user's ID
     */
    fun updateLastActivity(userId: UUID) {
        val user = userRepository.findById(userId)
            ?: throw UserNotFoundException(UserErrorCode.USER_NOT_FOUND)

        // Check if the user has registered or recorded a book in the last 7 days
        val sevenDaysAgo = LocalDateTime.now().minusDays(7)

        // Check for book registrations in the last 7 days
        val recentBooks = userBookRepository.findByUserIdAndCreatedAtAfter(userId, sevenDaysAgo)

        // Check for reading records in the last 7 days
        val userBooks = userBookRepository.findAllByUserId(userId)
        val userBookIds = userBooks.map { it.id.value }
        val recentRecords = if (userBookIds.isNotEmpty()) {
            readingRecordRepository.findByUserBookIdInAndCreatedAtAfter(userBookIds, sevenDaysAgo)
        } else {
            emptyList()
        }

        // Update lastActivity only if the user has registered or recorded a book in the last 7 days
        if (recentBooks.isNotEmpty() || recentRecords.isNotEmpty()) {
            userRepository.save(user.updateLastActivity())
        }
    }

    fun updateNotificationSettings(userId: UUID, notificationEnabled: Boolean): UserProfileVO {
        val user = userRepository.findById(userId)
            ?: throw UserNotFoundException(UserErrorCode.USER_NOT_FOUND)

        val updatedUser = userRepository.save(user.updateNotificationSettings(notificationEnabled))
        return UserProfileVO.newInstance(updatedUser)
    }
}
