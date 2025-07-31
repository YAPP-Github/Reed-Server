package org.yapp.infra.userbook.entity

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import jakarta.persistence.Index
import org.yapp.infra.common.BaseTimeEntity
import org.yapp.domain.userbook.BookStatus
import org.yapp.domain.userbook.UserBook
import java.sql.Types
import java.util.*

@Entity
@Table(
    name = "user_books",
    indexes = [
        Index(name = "idx_user_books_title", columnList = "title"),
        Index(name = "idx_user_books_user_id_title", columnList = "user_id, title")
    ]
)
@SQLDelete(sql = "UPDATE user_books SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
class UserBookEntity(
    @Id
    @JdbcTypeCode(Types.VARCHAR)
    @Column(length = 36, updatable = false, nullable = false)
    val id: UUID,

    @Column(name = "user_id", nullable = false, length = 36)
    @JdbcTypeCode(Types.VARCHAR)
    val userId: UUID,

    @Column(name = "book_id", nullable = false, length = 36)
    @JdbcTypeCode(Types.VARCHAR)
    val bookId: UUID,

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
        id = UserBook.Id.newInstance(this.id),
        userId = UserBook.UserId.newInstance(this.userId),
        bookId = UserBook.BookId.newInstance(this.bookId),
        bookIsbn = UserBook.BookIsbn.newInstance(this.bookIsbn),
        coverImageUrl = this.coverImageUrl,
        publisher = this.publisher,
        title = this.title,
        author = this.author,
        status = this.status,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deletedAt = this.deletedAt
    )

    companion object {
        fun fromDomain(userBook: UserBook): UserBookEntity {
            return UserBookEntity(
                id = userBook.id.value,
                userId = userBook.userId.value,
                bookId = userBook.bookId.value,
                bookIsbn = userBook.bookIsbn.value,
                coverImageUrl = userBook.coverImageUrl,
                publisher = userBook.publisher,
                title = userBook.title,
                author = userBook.author,
                status = userBook.status,
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserBookEntity) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}
