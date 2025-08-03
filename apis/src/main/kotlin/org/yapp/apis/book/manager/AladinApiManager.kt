package org.yapp.apis.book.manager

import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.yapp.infra.external.aladin.AladinApi
import org.yapp.infra.external.aladin.dto.AladinBookSearchRequest
import org.yapp.infra.external.aladin.request.AladinBookLookupRequest
import org.yapp.infra.external.aladin.response.AladinBookDetailResponse
import org.yapp.infra.external.aladin.response.AladinSearchResponse

@Component
class AladinApiManager(
    private val aladinApi: AladinApi
) {
    private val log = KotlinLogging.logger {}

    fun searchBooks(request: AladinBookSearchRequest): AladinSearchResponse {
        return aladinApi.searchBooks(request)
            .onSuccess { response ->
                log.info("Aladin search successful for query: '${request.query}', total results: ${response.totalResults}")
            }
            .getOrElse { exception ->
                log.error("Failed to call Aladin search API for request: '$request'", exception)
                throw IllegalStateException(
                    "Failed to retrieve search results from Aladin API: ${exception.message}",
                    exception
                )
            }
    }

    fun lookupBook(request: AladinBookLookupRequest): AladinBookDetailResponse {
        return aladinApi.lookupBook(request)
            .onSuccess { response ->
                log.info("Aladin lookup successful for itemId: '${request.itemId}', title: ${response.item?.firstOrNull()?.title}")
            }
            .getOrElse { exception ->
                log.error("Failed to call Aladin lookup API for request: '$request'", exception)
                throw IllegalStateException(
                    "Failed to retrieve book details from Aladin API: ${exception.message}",
                    exception
                )
            }
    }
}
