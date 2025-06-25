package org.yapp.global.util

import java.time.LocalDateTime

fun interface TimeProvider {
    fun now(): LocalDateTime
}
