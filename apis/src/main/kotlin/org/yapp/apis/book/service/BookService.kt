package org.yapp.apis.book.service

import BookSearchResponse
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.yapp.apis.book.dto.response.BookDetailResponse
import org.yapp.infra.external.aladin.helper.AladinApiHelper
import org.yapp.infra.external.aladin.response.AladinBookDetailResponse
import org.yapp.infra.external.aladin.response.AladinSearchResponse

@Service
class BookService(
    private val aladinApiHelper: AladinApiHelper,
) {
    private val log = KotlinLogging.logger {}


    fun searchBooks(query: String, params: Map<String, Any>): BookSearchResponse {
        log.info("Calling Aladin API for book search with query: $query")
        val response: AladinSearchResponse = aladinApiHelper.searchBooks(query, params)
        return BookSearchResponse.from(response)
    }

    fun lookupBook(itemId: String, itemIdType: String, optResult: List<String>?): BookDetailResponse {
        log.info("Calling Aladin API for book detail lookup for itemId: $itemId, itemIdType: $itemIdType with optResult: $optResult")
        val aladinResponse: AladinBookDetailResponse = aladinApiHelper.lookupBook(itemId, itemIdType, optResult)
        return BookDetailResponse.from(aladinResponse)
    }
}
