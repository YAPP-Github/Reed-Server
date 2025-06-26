package org.yapp.globalutils.exception

/**
 * Error response class for API responses.
 */
data class ErrorResponse(
    val status: Int,
    val code: String,
    val message: String
) {
    companion object {
        /**
         * Builder for ErrorResponse.
         */
        class Builder {
            private var status: Int = 0
            private var code: String = ""
            private var message: String = ""

            /**
             * Set the status code.
             *
             * @param status The HTTP status code.
             * @return The builder.
             */
            fun status(status: Int): Builder {
                this.status = status
                return this
            }

            /**
             * Set the error code.
             *
             * @param code The error code.
             * @return The builder.
             */
            fun code(code: String): Builder {
                this.code = code
                return this
            }

            /**
             * Set the error message.
             *
             * @param message The error message.
             * @return The builder.
             */
            fun message(message: String): Builder {
                this.message = message
                return this
            }

            /**
             * Build the ErrorResponse.
             *
             * @return The ErrorResponse.
             */
            fun build(): ErrorResponse {
                return ErrorResponse(status, code, message)
            }
        }

        /**
         * Create a new builder.
         *
         * @return The builder.
         */
        fun builder(): Builder {
            return Builder()
        }
    }
}
