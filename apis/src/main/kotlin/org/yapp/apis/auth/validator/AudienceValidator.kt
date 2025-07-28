package org.yapp.apis.auth.validator

import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.OAuth2ErrorCodes
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult
import org.springframework.security.oauth2.jwt.Jwt

class AudienceValidator(
    private val expectedAudience: String
) : OAuth2TokenValidator<Jwt> {

    override fun validate(token: Jwt): OAuth2TokenValidatorResult {
        return if (token.audience.contains(expectedAudience)) {
            OAuth2TokenValidatorResult.success()
        } else {
            val error = OAuth2Error(
                OAuth2ErrorCodes.INVALID_TOKEN, "The required audience is missing", null
            )
            OAuth2TokenValidatorResult.failure(error)
        }
    }
}
