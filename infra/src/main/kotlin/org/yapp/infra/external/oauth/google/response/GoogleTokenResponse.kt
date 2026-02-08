package org.yapp.infra.external.oauth.google.response

import com.fasterxml.jackson.annotation.JsonProperty

data class GoogleTokenResponse(
    @JsonProperty("access_token")
    val accessToken: String,
    
    @JsonProperty("refresh_token")
    val refreshToken: String?,
    
    @JsonProperty("expires_in")
    val expiresIn: Int,
    
    @JsonProperty("token_type")
    val tokenType: String,
    
    @JsonProperty("scope")
    val scope: String?,
    
    @JsonProperty("id_token")
    val idToken: String?
)
