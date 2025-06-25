package org.yapp.globalutils

import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class SystemTimeProvider : TimeProvider {
    override fun now(): LocalDateTime = LocalDateTime.now()
}
