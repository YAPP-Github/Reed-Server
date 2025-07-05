package org.yapp.infra.external.aladin

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.yapp.infra.external.aladin.dto.AladinBookLookupRequest
import org.yapp.infra.external.aladin.dto.AladinBookSearchRequest
import org.yapp.infra.external.aladin.response.AladinBookDetailResponse
import org.yapp.infra.external.aladin.response.AladinSearchResponse

@Component
class AladinApi(
    private val aladinRestClient: AladinRestClient,
    @Value("\${aladin.api.ttbkey:#{null}}")
    private var ttbKey: String? = null
) {
    fun searchBooks(request: AladinBookSearchRequest): Result<AladinSearchResponse> {
        return runCatching {
            val aladinApiParams = request.toMap()
            aladinRestClient.itemSearch(ttbKey, aladinApiParams) // Map으로 전달
        }
    }

    fun lookupBook(request: AladinBookLookupRequest): Result<AladinBookDetailResponse> {
        return runCatching {
            val aladinApiParams = request.toMap()
            aladinRestClient.itemLookUp(ttbKey, aladinApiParams) // Map으로 전달
        }
    }
}
