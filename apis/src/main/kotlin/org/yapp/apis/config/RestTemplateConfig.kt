package org.yapp.apis.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

/**
 * Configuration for RestTemplate.
 */
@Configuration
class RestTemplateConfig {
    
    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }
}