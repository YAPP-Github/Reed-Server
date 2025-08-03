package org.yapp.apis.book.service

import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.yapp.apis.book.dto.request.BookDetailRequest
import org.yapp.apis.book.dto.request.BookSearchRequest
import org.yapp.apis.book.dto.response.BookDetailResponse
import org.yapp.apis.book.dto.response.BookSearchResponse
import org.yapp.apis.book.manager.AladinApiManager
import org.yapp.infra.external.aladin.response.AladinBookDetailResponse
import org.yapp.infra.external.aladin.response.AladinSearchResponse

@Service
class AladinBookQueryService(
    private val aladinApiManager: AladinApiManager,
) : BookQueryService {
    private val log = KotlinLogging.logger {}

    override fun searchBooks(request: BookSearchRequest): BookSearchResponse {
        log.info("Service - Converting BookSearchRequest to AladinBookSearchRequest and calling Aladin API for book search.")
        val aladinSearchRequest = request.toAladinRequest()
        val response: AladinSearchResponse = aladinApiManager.searchBooks(aladinSearchRequest)
        return BookSearchResponse.from(response)
    }

    override fun getBookDetail(request: BookDetailRequest): BookDetailResponse {
        log.info("Service - Converting BookDetailRequest to AladinBookLookupRequest and calling Aladin API for book detail lookup.")
        val aladinLookupRequest = request.toAladinRequest()
        val aladinResponse: AladinBookDetailResponse = aladinApiManager.lookupBook(aladinLookupRequest)
        return BookDetailResponse.from(aladinResponse)
    }
}
