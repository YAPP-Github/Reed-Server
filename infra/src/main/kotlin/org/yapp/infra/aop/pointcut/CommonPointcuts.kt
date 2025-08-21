package org.yapp.infra.aop.pointcut

import org.aspectj.lang.annotation.Pointcut

object CommonPointcuts {
    @Pointcut(
        """
        within(@org.springframework.web.bind.annotation.RestController *) ||
        within(@org.springframework.stereotype.Controller *)
    """
    )
    fun controller() {
    }

    @Pointcut(
        """
        within(@org.yapp.globalutils.annotation.UseCase *) ||
        within(@org.yapp.globalutils.annotation.ApplicationService *) ||
        within(@org.yapp.globalutils.annotation.DomainService *) ||
        within(@org.springframework.stereotype.Service *)
    """
    )
    fun serviceLayer() {
    }

    @Pointcut("@annotation(org.yapp.globalutils.annotation.NoLogging) || @within(org.yapp.globalutils.annotation.NoLogging)")
    fun noLogging() {
    }
}
