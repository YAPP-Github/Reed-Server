package org.yapp.batch.service

data class FcmSendResult(
    val successCount: Int,
    val failureCount: Int,
    val invalidTokens: List<String>
)
