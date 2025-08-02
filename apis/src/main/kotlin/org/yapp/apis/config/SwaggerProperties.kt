package org.yapp.apis.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "swagger")
data class SwaggerProperties(
    val servers: List<ServerConfig> = emptyList(),
    val info: InfoConfig = InfoConfig()
) {
    data class ServerConfig(
        val url: String,
        val description: String
    )
    
    data class InfoConfig(
        val title: String = "YAPP API",
        val description: String = "YAPP API Documentation",
        val version: String = "v1"
    )
} 