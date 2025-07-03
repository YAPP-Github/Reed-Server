package org.yapp.infra.config.external.oauth

import org.apache.hc.client5.http.config.RequestConfig
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.core5.util.Timeout
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestClient
import org.yapp.infra.InfraBaseConfig

@Configuration
class RestClientConfig : InfraBaseConfig {

    @Bean
    fun restClient(): RestClient {
        val requestConfig = RequestConfig.custom()
            .setConnectionRequestTimeout(Timeout.ofDays(5000))
            .setResponseTimeout(Timeout.ofDays(5000))
            .build()

        val httpClient: CloseableHttpClient = HttpClients.custom()
            .setDefaultRequestConfig(requestConfig)
            .build()

        val factory = HttpComponentsClientHttpRequestFactory(httpClient)

        return RestClient.builder()
            .requestFactory(factory)
            .build()
    }
}
