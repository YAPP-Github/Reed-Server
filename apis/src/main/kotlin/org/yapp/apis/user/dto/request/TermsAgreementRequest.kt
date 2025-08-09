package org.yapp.apis.user.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

@Schema(
    name = "TermsAgreementRequest",
    description = "Request to update terms agreement status"
)
data class TermsAgreementRequest private constructor(
    @field:Schema(
        description = "Whether the user agrees to the terms of service",
        example = "true",
        required = true
    )
    @field:NotNull(message = "termsAgreed must not be null")
    val termsAgreed: Boolean? = null
) {
    fun validTermsAgreed(): Boolean = termsAgreed!!
}
