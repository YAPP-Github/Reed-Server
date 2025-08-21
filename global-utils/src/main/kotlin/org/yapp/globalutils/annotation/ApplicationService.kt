package org.yapp.globalutils.annotation

import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Service
@Validated
annotation class ApplicationService
