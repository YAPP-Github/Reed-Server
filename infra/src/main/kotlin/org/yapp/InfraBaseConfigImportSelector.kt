package org.yapp

import org.springframework.context.annotation.DeferredImportSelector
import org.springframework.core.type.AnnotationMetadata

internal class InfraBaseConfigImportSelector : DeferredImportSelector {

    override fun selectImports(metadata: AnnotationMetadata): Array<String> {
        return getValues(metadata)
            .map { it.configClass.name }
            .toTypedArray()
    }

    private fun getValues(metadata: AnnotationMetadata): Array<InfraBaseConfigGroup> {
        val attributes = metadata.getAnnotationAttributes(EnableInfraBaseConfig::class.java.name)
        @Suppress("UNCHECKED_CAST")
        return attributes?.get("value") as Array<InfraBaseConfigGroup>? ?: emptyArray()
    }
}
