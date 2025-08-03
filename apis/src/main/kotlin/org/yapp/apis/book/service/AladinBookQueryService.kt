package org.yapp.apis.book.service

import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.yapp.apis.book.dto.request.BookDetailRequest
import org.yapp.apis.book.dto.request.BookSearchRequest
import org.yapp.apis.book.dto.response.BookDetailResponse
import org.yapp.apis.book.dto.response.BookSearchResponse
import org.yapp.apis.book.exception.BookErrorCode
import org.yapp.apis.book.exception.BookException
import org.yapp.infra.external.aladin.AladinApi
import org.yapp.infra.external.aladin.request.AladinBookLookupRequest
import org.yapp.infra.external.aladin.request.AladinBookSearchRequest
import org.yapp.infra.external.aladin.response.AladinBookDetailResponse
import org.yapp.infra.external.aladin.response.AladinSearchResponse

@Service
class AladinBookQueryService(
    private val aladinApi: AladinApi
) : BookQueryService {
    private val log = KotlinLogging.logger {}

    override fun searchBooks(request: BookSearchRequest): BookSearchResponse {
        log.info("Service - Converting BookSearchRequest to AladinBookSearchRequest and calling Aladin API for book search.")
        val aladinSearchRequest = AladinBookSearchRequest.of(
            request.validQuery(),
            request.queryType,
            request.searchTarget,
            request.maxResults,
            request.start,
            request.sort,
            request.categoryId
        )
        val response: AladinSearchResponse = aladinApi.searchBooks(aladinSearchRequest)
            .onSuccess { response ->
                log.info("Aladin search successful for query: '${request.query}', total results: ${response.totalResults}")
            }
            .getOrElse { exception ->
                log.error("Failed to call Aladin search API for request: '$request'", exception)
                throw BookException(BookErrorCode.ALADIN_API_SEARCH_FAILED, exception.message)
            }
        return BookSearchResponse.from(response)
    }

    override fun getBookDetail(request: BookDetailRequest): BookDetailResponse {
        log.info("Service - Converting BookDetailRequest to AladinBookLookupRequest and calling Aladin API for book detail lookup.")
        val aladinLookupRequest = AladinBookLookupRequest.from(request.validIsbn())
        val response: AladinBookDetailResponse = aladinApi.lookupBook(aladinLookupRequest)
            .onSuccess { response ->
                log.info("Aladin lookup successful for itemId: '${aladinLookupRequest.itemId}', title: ${response.item?.firstOrNull()?.title}")
            }
            .getOrElse { exception ->
                log.error("Failed to call Aladin lookup API for request: '$request'", exception)
                throw BookException(BookErrorCode.ALADIN_API_LOOKUP_FAILED, exception.message)
            }
        return BookDetailResponse.from(response)
    }
}
