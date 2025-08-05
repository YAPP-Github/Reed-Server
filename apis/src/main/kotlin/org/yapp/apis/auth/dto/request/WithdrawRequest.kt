package org.yapp.apis.auth.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.yapp.domain.user.ProviderType

@Schema(
    name = "WithdrawRequest",
    description = "DTO for user withdrawal requests"
)
data class WithdrawRequest private constructor(
    @Schema(
        description = "Type of social login provider for withdrawal",
        example = "APPLE",
        required = true
    )
    @field:NotNull(message = "Provider type is not-null")
    val providerType: ProviderType? = null
) {
    fun validProviderType(): ProviderType = providerType!!
} 
