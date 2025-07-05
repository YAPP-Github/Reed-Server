package org.yapp.apis.book.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern // Pattern 어노테이션 추가
import org.yapp.globalutils.util.RegexUtils
import org.yapp.infra.external.aladin.dto.AladinBookLookupRequest

data class BookDetailRequest private constructor(
    @field:NotBlank(message = "아이템 ID는 필수입니다.")
    @field:Pattern(
        regexp = RegexUtils.NOT_BLANK_AND_NOT_NULL_STRING_PATTERN,
        message = "아이템 ID는 유효한 ISBN 형식이 아닙니다."
    )
    val itemId: String? = null,
    val itemIdType: String? = "ISBN",
    val optResult: List<String>? = null
) {
    fun toAladinRequest(): AladinBookLookupRequest {
        return AladinBookLookupRequest.create(
            itemId = this.itemId!!,
            itemIdType = this.itemIdType ?: "ISBN",
            optResult = this.optResult
        )
    }
}
