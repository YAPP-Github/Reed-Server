package org.yapp.infra.userbook.entity

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.annotations.SQLDelete
import org.yapp.domain.userbook.BookStatus
import org.yapp.domain.common.BaseTimeEntity
import org.yapp.domain.userbook.UserBook
import java.sql.Types
import java.util.*

@Entity
@Table(name = "user_books")
@SQLDelete(sql = "UPDATE user_books SET deleted_at = NOW() WHERE id = ?")
class UserBookEntity(
    @Id
    @JdbcTypeCode(Types.VARCHAR)
    @Column(length = 36, updatable = false, nullable = false)
    val id: UUID,

    @Column(name = "user_id", nullable = false, length = 36)
    @JdbcTypeCode(Types.VARCHAR)
    val userId: UUID,

    @Column(name = "book_isbn", nullable = false)
    val bookIsbn: String,

    coverImageUrl: String,
    publisher: String,
    title: String,
    author: String,
    status: BookStatus
) : BaseTimeEntity() {

    @Column(name = "cover_image_url", nullable = false, length = 2048)
    var coverImageUrl: String = coverImageUrl
        protected set

    @Column(name = "publisher", nullable = false, length = 300)
    var publisher: String = publisher
        protected set

    @Column(nullable = false, length = 255)
    var title: String = title
        protected set

    @Column(length = 255)
    var author: String = author
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: BookStatus = status
        protected set

    fun toDomain(): UserBook = UserBook.reconstruct(
        id = id,
        userId = userId,
        bookIsbn = bookIsbn,
        status = status,
        coverImageUrl = coverImageUrl,
        publisher = publisher,
        title = title,
        author = author,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = deletedAt
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserBookEntity) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    companion object {
        fun fromDomain(userBook: UserBook): UserBookEntity {
            return UserBookEntity(
                id = userBook.id,
                userId = userBook.userId,
                bookIsbn = userBook.bookIsbn,
                status = userBook.status,
                coverImageUrl = userBook.coverImageUrl,
                publisher = userBook.publisher,
                title = userBook.title,
                author = userBook.author,
            ).apply {
                this.createdAt = userBook.createdAt
                this.updatedAt = userBook.updatedAt
                this.deletedAt = userBook.deletedAt
            }
        }
    }
}
