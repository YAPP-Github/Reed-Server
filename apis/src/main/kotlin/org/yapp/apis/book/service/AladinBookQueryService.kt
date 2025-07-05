package org.yapp.apis.book.service

import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.yapp.apis.book.dto.request.BookDetailRequest
import org.yapp.apis.book.dto.request.BookSearchRequest
import org.yapp.apis.book.dto.response.BookDetailResponse
import org.yapp.apis.book.dto.response.BookSearchResponse
import org.yapp.infra.external.aladin.helper.AladinApiHelper
import org.yapp.infra.external.aladin.response.AladinBookDetailResponse
import org.yapp.infra.external.aladin.response.AladinSearchResponse

@Service
class AladinBookQueryService(
    private val aladinApiHelper: AladinApiHelper,
) {
    private val log = KotlinLogging.logger {}

    fun searchBooks(request: BookSearchRequest): BookSearchResponse {
        log.info("Service - Converting BookSearchRequest to AladinBookSearchRequest and calling Aladin API for book search.")
        val aladinSearchRequest = request.toAladinRequest()
        val response: AladinSearchResponse = aladinApiHelper.searchBooks(aladinSearchRequest)
        return BookSearchResponse.from(response)
    }

    fun lookupBook(request: BookDetailRequest): BookDetailResponse {
        log.info("Service - Converting BookDetailRequest to AladinBookLookupRequest and calling Aladin API for book detail lookup.")
        val aladinLookupRequest = request.toAladinRequest()
        val aladinResponse: AladinBookDetailResponse = aladinApiHelper.lookupBook(aladinLookupRequest)
        return BookDetailResponse.from(aladinResponse)
    }
}
