package org.yapp.infra.config.internal.page

import org.springframework.context.annotation.Configuration
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.yapp.infra.InfraBaseConfig

@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
class PageConfig : InfraBaseConfig {
}
