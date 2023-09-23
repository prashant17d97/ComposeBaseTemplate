package com.example.basetemplate.network

/**
 * A generic class that represents a resource with status, data, message, error details, and status code.
 *
 * @param T The type of data contained in the resource.
 * @property status The current status of the resource (e.g., SUCCESS, ERROR, LOADING, FAILURE).
 * @property data The data associated with the resource (default is null).
 * @property message A message providing additional information about the resource (default is null).
 * @property errorDetails Details about the error in case of an error status (default is null).
 * @property statusCode The HTTP status code associated with the resource (default is null).
 */
class Resource<T> private constructor(
    val status: Status,
    val data: T? = null,
    val message: String? = null,
    val errorDetails: String? = null,
    val statusCode: Int? = null
) {
    companion object {
        /**
         * Creates a success resource with data, message, and status code.
         *
         * @param data The data associated with the resource.
         * @param message A message providing additional information about the resource.
         * @param statusCode The HTTP status code associated with the resource.
         * @return A success resource.
         */
        fun <T> success(data: T?, message: String?, statusCode: Int?): Resource<T> {
            return Resource(
                status = Status.SUCCESS,
                data = data,
                message = message,
                errorDetails = null,
                statusCode = statusCode
            )
        }

        /**
         * Creates an error resource with a message, error details, and status code.
         *
         * @param message A message indicating the error.
         * @param errorDetails Details about the error.
         * @param statusCode The HTTP status code associated with the error.
         * @return An error resource.
         */
        fun <T> error(message: String?, errorDetails: String?, statusCode: Int?): Resource<T> {
            return Resource(
                status = Status.ERROR,
                data = null,
                message = message,
                errorDetails = errorDetails,
                statusCode = statusCode
            )
        }

        /**
         * Creates a failure resource with a message, error details, and status code.
         *
         * @param message A message indicating the failure.
         * @param errorDetails Details about the failure.
         * @param statusCode The HTTP status code associated with the failure.
         * @return A failure resource.
         */
        fun <T> failure(message: String?, errorDetails: String?, statusCode: Int?): Resource<T> {
            return Resource(
                status = Status.FAILURE,
                data = null,
                message = message,
                errorDetails = errorDetails,
                statusCode = statusCode
            )
        }
    }
}

/**
 * An enum class representing the status of a resource.
 */
enum class Status {
    SUCCESS, ERROR, LOADING, FAILURE
}
