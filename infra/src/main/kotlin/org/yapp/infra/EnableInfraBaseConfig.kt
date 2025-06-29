package org.yapp.infra

import org.springframework.context.annotation.Import

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Import(InfraBaseConfigImportSelector::class)
annotation class EnableInfraBaseConfig(
    val value: Array<InfraBaseConfigGroup>
)
