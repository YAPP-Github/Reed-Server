package org.yapp.infra.config.internal.async

import org.slf4j.MDC
import org.springframework.core.task.TaskDecorator

class MdcTaskDecorator : TaskDecorator {

    override fun decorate(runnable: Runnable): Runnable {
        val contextMap = MDC.getCopyOfContextMap()

        return Runnable {
            val previous = MDC.getCopyOfContextMap()

            try {
                contextMap?.let(MDC::setContextMap) ?: MDC.clear()
                runnable.run()
            } finally {
                previous?.let(MDC::setContextMap) ?: MDC.clear()
            }
        }
    }
}
