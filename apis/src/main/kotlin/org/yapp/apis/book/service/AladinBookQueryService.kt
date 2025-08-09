package org.yapp.apis.book.service

import mu.KotlinLogging
import org.yapp.apis.book.dto.request.BookDetailRequest
import org.yapp.apis.book.dto.request.BookSearchRequest
import org.yapp.apis.book.dto.response.BookDetailResponse
import org.yapp.apis.book.dto.response.BookSearchResponse
import org.yapp.apis.book.exception.BookErrorCode
import org.yapp.apis.book.exception.BookException
import org.yapp.apis.book.util.IsbnConverter
import org.yapp.globalutils.annotation.ApplicationService
import org.yapp.globalutils.validator.IsbnValidator
import org.yapp.infra.external.aladin.AladinApi
import org.yapp.infra.external.aladin.request.AladinBookLookupRequest
import org.yapp.infra.external.aladin.request.AladinBookSearchRequest
import org.yapp.infra.external.aladin.response.AladinBookDetailResponse
import org.yapp.infra.external.aladin.response.AladinSearchItem
import org.yapp.infra.external.aladin.response.AladinSearchResponse

@ApplicationService
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

        val filteredItems = response.item.filter { item ->
            getValidAndFilteredIsbn13(item) != null
        }

        val filteredResponse = AladinSearchResponse(
            version = response.version,
            title = response.title,
            link = response.link,
            pubDate = response.pubDate,
            totalResults = response.totalResults,
            startIndex = response.startIndex,
            itemsPerPage = response.itemsPerPage,
            query = response.query,
            searchCategoryId = response.searchCategoryId,
            searchCategoryName = response.searchCategoryName,
            item = filteredItems
        )

        return BookSearchResponse.from(filteredResponse)
    }

    override fun getBookDetail(request: BookDetailRequest): BookDetailResponse {
        log.info("Service - Converting BookDetailRequest to AladinBookLookupRequest and calling Aladin API for book detail lookup.")
        val aladinLookupRequest = AladinBookLookupRequest.from(request.validIsbn13())
        val response: AladinBookDetailResponse = aladinApi.lookupBook(aladinLookupRequest)
            .onSuccess { response ->
                log.info("Aladin lookup successful for itemId: '${aladinLookupRequest.itemId}', title: ${response.item.firstOrNull()?.title}")
            }
            .getOrElse { exception ->
                log.error("Failed to call Aladin lookup API for request: '$request'", exception)
                throw BookException(BookErrorCode.ALADIN_API_LOOKUP_FAILED, exception.message)
            }
        return BookDetailResponse.from(response)
    }


    private fun getValidAndFilteredIsbn13(item: AladinSearchItem): String? {
        val primaryIsbn13 = item.isbn13
            ?.takeIf { it.isNotBlank() && IsbnValidator.isValidIsbn13(it) }

        val convertedIsbn13 = item.isbn
            ?.takeIf { it.isNotBlank() && IsbnValidator.isValidIsbn10(it) }
            ?.let { validIsbn10 -> IsbnConverter.toIsbn13(validIsbn10) }
            ?.takeIf { IsbnValidator.isValidIsbn13(it) }

        return primaryIsbn13 ?: convertedIsbn13
    }
}
