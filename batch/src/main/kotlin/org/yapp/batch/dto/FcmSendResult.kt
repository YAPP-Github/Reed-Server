package org.yapp.batch.dto

data class FcmSendResult private constructor(
    val successCount: Int,
    val failureCount: Int,
    val invalidTokens: List<String>
) {
    companion object {
        private const val ZERO_COUNT = 0

        fun of(
            successCount: Int,
            failureCount: Int,
            invalidTokens: List<String>
        ): FcmSendResult {
            return FcmSendResult(successCount, failureCount, invalidTokens)
        }

        fun empty(): FcmSendResult {
            return FcmSendResult(
                successCount = ZERO_COUNT,
                failureCount = ZERO_COUNT,
                invalidTokens = emptyList()
            )
        }

        fun allFailed(failureCount: Int): FcmSendResult {
            return FcmSendResult(
                successCount = ZERO_COUNT,
                failureCount = failureCount,
                invalidTokens = emptyList()
            )
        }
    }
}
