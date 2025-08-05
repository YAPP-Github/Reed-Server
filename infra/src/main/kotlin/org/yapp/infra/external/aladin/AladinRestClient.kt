package org.yapp.infra.external.aladin

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.util.UriComponentsBuilder
import org.yapp.infra.external.aladin.response.AladinBookDetailResponse
import org.yapp.infra.external.aladin.response.AladinSearchResponse

@Component
class AladinRestClient(
    @Qualifier("aladinApiRestClient")
    private val restClient: RestClient
) {
    private val API_VERSION = "20131101"
    private val DEFAULT_OUTPUT_FORMAT = "JS"

    fun itemSearch(
        ttbKey: String?,
        params: Map<String, Any>
    ): AladinSearchResponse {
        val uriBuilder = UriComponentsBuilder.fromUriString("/ItemSearch.aspx")
            .queryParam("ttbkey", ttbKey)

        uriBuilder.addCommonQueryParams(params)

        return restClient.get()
            .uri(uriBuilder.build().toUriString())
            .retrieve()
            .body(AladinSearchResponse::class.java)
            ?: throw IllegalStateException("Aladin ItemSearch API 응답이 null 입니다.")
    }

    fun itemLookUp(
        ttbKey: String?,
        params: Map<String, Any> = emptyMap()
    ): AladinBookDetailResponse {
        val uriBuilder = UriComponentsBuilder.fromUriString("/ItemLookUp.aspx")
            .queryParam("ttbkey", ttbKey)

        uriBuilder.addCommonQueryParams(params)

        return restClient.get()
            .uri(uriBuilder.build().toUriString())
            .retrieve()
            .body(AladinBookDetailResponse::class.java)
            ?: throw IllegalStateException("Aladin ItemLookUp API 응답이 null 입니다.")
    }

    private fun UriComponentsBuilder.addCommonQueryParams(params: Map<String, Any>) {
        params.forEach { (key, value) ->
            if (key == "OptResult" && value is List<*>) {
                this.queryParam(key, value.joinToString(","))
            } else {
                this.queryParam(key, value)
            }
        }
        this.queryParam("Output", DEFAULT_OUTPUT_FORMAT)
            .queryParam("Version", API_VERSION)
    }
}
