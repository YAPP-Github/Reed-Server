package org.yapp.globalutils

import java.time.LocalDateTime

fun interface TimeProvider {
    fun now(): LocalDateTime
}
