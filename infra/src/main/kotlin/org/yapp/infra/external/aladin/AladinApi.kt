package org.yapp.infra.external.aladin

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.yapp.infra.external.aladin.request.AladinBookLookupRequest
import org.yapp.infra.external.aladin.request.AladinBookSearchRequest
import org.yapp.infra.external.aladin.response.AladinBookDetailResponse
import org.yapp.infra.external.aladin.response.AladinSearchResponse

@Component
class AladinApi(
    private val aladinRestClient: AladinRestClient,
    @Value("\${aladin.api.ttb-key}")
    private var ttbKey: String
) {
    fun searchBooks(request: AladinBookSearchRequest): Result<AladinSearchResponse> {
        return runCatching {
            val aladinApiParams = request.toMap()
            aladinRestClient.itemSearch(ttbKey, aladinApiParams)
        }
    }

    fun lookupBook(request: AladinBookLookupRequest): Result<AladinBookDetailResponse> {
        return runCatching {
            val aladinApiParams = request.toMap()
            aladinRestClient.itemLookUp(ttbKey, aladinApiParams)
        }
    }
}
