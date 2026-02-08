package org.yapp.domain.user

import org.yapp.domain.readingrecord.ReadingRecordRepository
import org.yapp.domain.user.exception.UserErrorCode
import org.yapp.domain.user.exception.UserNotFoundException
import org.yapp.domain.user.vo.*
import org.yapp.domain.userbook.UserBookRepository
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

    fun findNotificationTargetUserById(id: UUID): NotificationTargetUserVO {
        val user = userRepository.findById(id) ?: throw UserNotFoundException(UserErrorCode.USER_NOT_FOUND)
        return NotificationTargetUserVO.from(user)
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

    fun updateGoogleRefreshToken(userId: UUID, refreshToken: String): UserAuthVO {
        val user = userRepository.findById(userId)
            ?: throw UserNotFoundException(UserErrorCode.USER_NOT_FOUND)

        val updatedUser = userRepository.save(user.updateGoogleRefreshToken(refreshToken))

        return UserAuthVO.newInstance(updatedUser)
    }

    fun deleteUser(userId: UUID) {
        val user = userRepository.findById(userId)
            ?: throw UserNotFoundException(UserErrorCode.USER_NOT_FOUND)

        userRepository.deleteById(user.id.value)
    }

    fun updateLastActivity(userId: UUID) {
        val user = userRepository.findById(userId)
            ?: throw UserNotFoundException(UserErrorCode.USER_NOT_FOUND)

        val sevenDaysAgo = LocalDateTime.now().minusDays(7)

        val recentBooks = userBookRepository.findByUserIdAndCreatedAtAfter(userId, sevenDaysAgo)

        val userBooks = userBookRepository.findAllByUserId(userId)
        val userBookIds = userBooks.map { it.id.value }
        val recentRecords = if (userBookIds.isNotEmpty()) {
            readingRecordRepository.findByUserBookIdInAndCreatedAtAfter(userBookIds, sevenDaysAgo)
        } else {
            emptyList()
        }

        if (recentBooks.isNotEmpty() || recentRecords.isNotEmpty()) {
            userRepository.save(user.updateLastActivity())
        }
    }

    fun forceUpdateLastActivity(userId: UUID, newLastActivity: LocalDateTime) {
        val user = userRepository.findById(userId)
            ?: throw UserNotFoundException(UserErrorCode.USER_NOT_FOUND)

        userRepository.save(user.forceUpdateLastActivity(newLastActivity))
    }

    fun updateNotificationSettings(userId: UUID, notificationEnabled: Boolean): UserProfileVO {
        val user = userRepository.findById(userId)
            ?: throw UserNotFoundException(UserErrorCode.USER_NOT_FOUND)

        val updatedUser = userRepository.save(user.updateNotificationEnabled(notificationEnabled))
        return UserProfileVO.newInstance(updatedUser)
    }

    fun findUnrecordedUsers(daysThreshold: Int): List<NotificationTargetUserVO> {
        val targetDate = LocalDateTime.now().minusDays(daysThreshold.toLong())

        val allUsers = userRepository.findByLastActivityBeforeAndNotificationEnabled(
            LocalDateTime.now().plusDays(1),
            true
        )

        return allUsers.filter { user ->
            val userId = user.id.value
            val recentBooks = userBookRepository.findByUserIdAndCreatedAtAfter(userId, targetDate)

            if (recentBooks.isEmpty()) {
                false
            } else {
                val userBooks = userBookRepository.findAllByUserId(userId)
                val userBookIds = userBooks.map { it.id.value }

                val recentRecords = if (userBookIds.isNotEmpty()) {
                    readingRecordRepository.findByUserBookIdInAndCreatedAtAfter(userBookIds, targetDate)
                } else {
                    emptyList()
                }

                recentRecords.isEmpty()
            }
        }.map { NotificationTargetUserVO.from(it) }
    }

    fun findDormantUsers(daysThreshold: Int): List<NotificationTargetUserVO> {
        val targetDate = LocalDateTime.now().minusDays(daysThreshold.toLong())
        return userRepository.findByLastActivityBeforeAndNotificationEnabled(targetDate, true)
            .map { NotificationTargetUserVO.from(it) }
    }
}
