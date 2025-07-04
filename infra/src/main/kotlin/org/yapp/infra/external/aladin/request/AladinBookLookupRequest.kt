package org.yapp.infra.external.aladin.dto

/**
 * 알라딘 ItemLookUp API 호출을 위한 요청 DTO.
 * 내부적으로 알라딘 API 파라미터 규칙에 맞게 변환하는 책임을 가집니다.
 */
data class AladinBookLookupRequest private constructor( // private constructor 유지
    val itemId: String,
    val itemIdType: String,
    val optResult: List<String>?
) {
    fun toMap(): Map<String, Any> {
        val params = mutableMapOf<String, Any>()
        params["ItemId"] = itemId
        params["ItemIdType"] = itemIdType
        optResult?.let {
            if (it.isNotEmpty()) {
                params["OptResult"] = it
            }
        }
        return params
    }

    companion object {
        fun create(
            itemId: String,
            itemIdType: String,
            optResult: List<String>? = null
        ): AladinBookLookupRequest {
            return AladinBookLookupRequest(itemId, itemIdType, optResult)
        }
    }
}
