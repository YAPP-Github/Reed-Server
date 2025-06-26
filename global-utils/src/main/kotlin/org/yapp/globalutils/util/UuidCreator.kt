package org.yapp.globalutils.util

import com.github.f4b6a3.uuid.UuidCreator
import java.util.*

object UuidGenerator {
    fun create(): UUID = UuidCreator.getTimeOrderedEpoch()// UUIDv7
}
