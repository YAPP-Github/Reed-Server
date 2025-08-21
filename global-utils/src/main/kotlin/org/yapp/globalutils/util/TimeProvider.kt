package org.yapp.globalutils.util

import java.time.LocalDateTime

fun interface TimeProvider {
    fun now(): LocalDateTime
}
