package org.yapp.infra.external.aladin

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.yapp.infra.external.aladin.response.AladinBookDetailResponse
import org.yapp.infra.external.aladin.response.AladinSearchResponse

@Component
class AladinApi(
    private val aladinRestClient: AladinRestClient,
    @Value("\${aladin.api.ttbkey}")
    private val ttbKey: String
) {
    fun searchBooks(
        query: String,
        params: Map<String, Any> = emptyMap()
    ): Result<AladinSearchResponse> {
        return runCatching {
            aladinRestClient.itemSearch(ttbKey, query, params)
        }
    }

    fun lookupBook(
        itemId: String,
        itemIdType: String,
        optResult: List<String>?
    ): Result<AladinBookDetailResponse> {
        return runCatching {
            // 여기서 Map으로 변환하여 RestClient로 전달
            val aladinApiParams = mutableMapOf<String, Any>()
            optResult?.let {
                if (it.isNotEmpty()) {
                    aladinApiParams["OptResult"] = it
                }
            }
            aladinRestClient.itemLookUp(ttbKey, itemId, itemIdType, aladinApiParams)
        }
    }
}
