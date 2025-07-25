package org.yapp.apis.auth.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

@Schema(description = "Request to update terms agreement status")
data class TermsAgreementRequest private constructor(
    @Schema(description = "Whether the user agrees to the terms of service", example = "true", required = true)
    val termsAgreed: Boolean? = null


) {
    fun validTermsAgreed(): Boolean = termsAgreed!!
}
