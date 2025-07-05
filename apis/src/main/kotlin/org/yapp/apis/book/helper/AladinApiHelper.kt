package org.yapp.infra.external.aladin.helper

import mu.KotlinLogging
import org.yapp.globalutils.annotation.Helper
import org.yapp.infra.external.aladin.AladinApi
import org.yapp.infra.external.aladin.dto.AladinBookLookupRequest // Import Aladin DTOs
import org.yapp.infra.external.aladin.dto.AladinBookSearchRequest  // Import Aladin DTOs
import org.yapp.infra.external.aladin.response.AladinBookDetailResponse
import org.yapp.infra.external.aladin.response.AladinSearchResponse


@Helper
class AladinApiHelper(
    private val aladinApi: AladinApi
) {
    private val log = KotlinLogging.logger {}

    fun searchBooks(request: AladinBookSearchRequest): AladinSearchResponse {
        return aladinApi.searchBooks(request) // Pass the DTO directly to AladinApi
            .onSuccess { response ->
                log.info("Aladin search successful for query: '${request.query}', total results: ${response.totalResults}")
            }
            .getOrElse { exception ->
                log.error("Failed to call Aladin search API for request: '$request'", exception)
                // TODO: 특정 비즈니스 예외로 맵핑하거나, 공통 예외 처리 계층에서 처리하도록 리팩토링
                throw IllegalStateException(
                    "Failed to retrieve search results from Aladin API: ${exception.message}",
                    exception
                )
            }
    }

    fun lookupBook(request: AladinBookLookupRequest): AladinBookDetailResponse {
        return aladinApi.lookupBook(request) // Pass the DTO directly to AladinApi
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
