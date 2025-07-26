package org.yapp.infra.book.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import org.yapp.domain.book.Book
import org.yapp.infra.common.BaseTimeEntity
import java.sql.Types
import java.util.UUID

@Entity
@Table(name = "books")
@SQLDelete(sql = "UPDATE books SET deleted_at = NOW() WHERE isbn = ?")
@SQLRestriction("deleted_at IS NULL")
class BookEntity private constructor(
    @Id
    @JdbcTypeCode(Types.VARCHAR)
    @Column(length = 36, updatable = false, nullable = false)
    val id: UUID,

    @Column(length = 13, updatable = false, nullable = false, unique = true)
    val isbn: String,

    title: String,
    author: String,
    publisher: String,
    publicationYear: Int? = null,
    coverImageUrl: String,
    description: String? = null,
) : BaseTimeEntity() {

    @Column(nullable = false, length = 255)
    var title: String = title
        protected set

    @Column(length = 255)
    var author: String = author
        protected set

    @Column(length = 255)
    var publisher: String = publisher
        protected set

    @Column(name = "publication_year")
    var publicationYear: Int? = publicationYear
        protected set

    @Column(name = "cover_image_url", length = 2048)
    var coverImageUrl: String = coverImageUrl
        protected set

    @Column(length = 2000)
    var description: String? = description
        protected set


    fun toDomain(): Book = Book.reconstruct(
        id = Book.Id.newInstance(this.id),
        isbn = Book.Isbn.newInstance(this.isbn),
        title = title,
        author = author,
        publisher = publisher,
        publicationYear = publicationYear,
        coverImageUrl = coverImageUrl,
        description = description,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = deletedAt
    )

    companion object {
        fun fromDomain(book: Book): BookEntity = BookEntity(
            id = book.id.value,
            isbn = book.isbn.value,
            title = book.title,
            author = book.author,
            publisher = book.publisher,
            publicationYear = book.publicationYear,
            coverImageUrl = book.coverImageUrl,
            description = book.description,
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BookEntity) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}
