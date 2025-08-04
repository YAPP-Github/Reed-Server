package org.yapp.apis.book.dto.response

import org.yapp.apis.util.AuthorExtractor
import org.yapp.apis.util.IsbnConverter
import org.yapp.domain.userbook.BookStatus
import org.yapp.infra.external.aladin.response.AladinBookDetailResponse

data class BookDetailResponse private constructor(
    val version: String?,
    val title: String,
    val link: String,
    val author: String?,
    val pubDate: String,
    val description: String,
    val isbn13: String?,
    val mallType: String,
    val coverImageUrl: String,
    val categoryName: String,
    val publisher: String?,
    val totalPage: Int?,
    val userBookStatus: BookStatus
) {
    fun withUserBookStatus(newUserBookStatus: BookStatus): BookDetailResponse {
        return this.copy(userBookStatus = newUserBookStatus)
    }

    companion object {
        fun from(response: AladinBookDetailResponse, userBookStatus: BookStatus = BookStatus.BEFORE_REGISTRATION): BookDetailResponse {
            val item = response.item.firstOrNull()
                ?: throw IllegalArgumentException("No book item found in detail response.")

            return BookDetailResponse(
                version = response.version,
                title = item.title,
                link = item.link,
                author = AuthorExtractor.extractAuthors(item.author),
                pubDate = item.pubDate ?: "",
                description = item.description ?: "",
                mallType = item.mallType,
                isbn13 = item.isbn13 ?: IsbnConverter.toIsbn13(item.isbn) ?: throw IllegalArgumentException("Either isbn13 or isbn must be provided"),
                coverImageUrl = item.cover,
                categoryName = item.categoryName,
                publisher = item.publisher ?: "",
                totalPage = item.subInfo.itemPage ?: 4032,
                userBookStatus = userBookStatus
            )
        }
    }
}
