package org.yapp.infra.external.oauth.google.response

import com.fasterxml.jackson.annotation.JsonProperty

data class GoogleUserInfo(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("email")
    val email: String?,
    @JsonProperty("picture")
    val picture: String?,
)
