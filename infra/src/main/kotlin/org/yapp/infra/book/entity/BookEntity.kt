package org.yapp.infra.book.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.annotations.SQLDelete
import org.yapp.domain.book.Book
import org.yapp.domain.common.BaseTimeEntity
import java.sql.Types

@Entity
@Table(name = "books")
@SQLDelete(sql = "UPDATE books SET deleted_at = NOW() WHERE isbn = ?")
class BookEntity private constructor(
    @Id
    @JdbcTypeCode(Types.VARCHAR)
    @Column(length = 13, updatable = false, nullable = false) // ISBN/ISBN13 length
    val isbn: String,
    @Column(nullable = false, length = 255)
    val title: String,

    @Column(length = 255)
    val author: String? = null,

    @Column(length = 255)
    val publisher: String? = null,

    @Column(name = "publication_year")
    val publicationYear: Int? = null,

    @Column(name = "cover_image_url", length = 2048)
    val coverImageUrl: String? = null,

    @Column(length = 2000)
    val description: String? = null
) : BaseTimeEntity() {

    fun toDomain(): Book = Book.reconstruct(
        isbn = isbn,
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
            isbn = book.isbn,
            title = book.title,
            author = book.author,
            publisher = book.publisher,
            publicationYear = book.publicationYear,
            coverImageUrl = book.coverImageUrl,
            description = book.description
        ).apply {
            this.createdAt = book.createdAt
            this.updatedAt = book.updatedAt
            this.deletedAt = book.deletedAt
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BookEntity) return false
        return isbn == other.isbn
    }

    override fun hashCode(): Int = isbn.hashCode()
}
