package org.yapp.infra.userbook.repository.impl

import org.springframework.stereotype.Repository
import org.yapp.domain.userbook.UserBookRepository
import org.yapp.domain.userbook.UserBook
import org.yapp.infra.userbook.entity.UserBookEntity
import org.yapp.infra.userbook.repository.JpaUserBookRepository
import java.util.*

@Repository
class UserBookRepositoryImpl(
    private val jpaUserBookRepository: JpaUserBookRepository
) : UserBookRepository {

    override fun findByUserIdAndBookIsbn(userId: UUID, isbn: String): UserBook? {
        return jpaUserBookRepository.findByUserIdAndBookIsbn(userId, isbn)?.toDomain()

    }

    override fun save(userBook: UserBook): UserBook {
        return jpaUserBookRepository.save(UserBookEntity.fromDomain(userBook)).toDomain()
    }

    override fun findAllByUserId(userId: UUID): List<UserBook> {
        return jpaUserBookRepository.findAllByUserId(userId).map { it.toDomain() }
    }
}
