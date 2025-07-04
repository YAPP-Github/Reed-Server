package org.yapp.apis.book.dto.request

import jakarta.validation.constraints.NotBlank
import org.yapp.infra.external.aladin.dto.AladinBookLookupRequest

data class BookDetailRequest private constructor(
    @field:NotBlank(message = "아이템 ID는 필수입니다.")
    val itemId: String? = null,
    val itemIdType: String? = "ISBN",
    val optResult: List<String>? = null
) {
    fun toAladinRequest(): AladinBookLookupRequest {
        require(!itemId.isNullOrBlank()) { "아이템 ID는 비어있을 수 없습니다." }
        return AladinBookLookupRequest.create(
            itemId = this.itemId,
            itemIdType = this.itemIdType ?: "ISBN",
            optResult = this.optResult
        )
    }
}
