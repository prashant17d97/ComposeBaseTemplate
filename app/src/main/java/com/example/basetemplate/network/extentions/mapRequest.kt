package com.example.basetemplate.network.extentions

import com.example.basetemplate.network.Resource
import retrofit2.Response

/**
 * Extension function for mapping a Retrofit [Response] to a [Resource] based on the HTTP status code.
 *
 * @param T The type of data contained in the response.
 * @return A [Resource] representing the response data with appropriate status and details.
 */
internal fun <T> Response<T>.mapToResource(): Resource<T> {
    return when (code()) {
        in 200..299 -> Resource.success(data = body(), message = message(), statusCode = code())
        in 400..499 -> Resource.error(
            message = message(),
            statusCode = code(),
            errorDetails = errorBody()?.string()
        )

        in 500..599 -> Resource.failure(
            message = message(),
            statusCode = code(),
            errorDetails = errorBody()?.string()
        )

        else -> Resource.error(
            message = "Unknown error",
            statusCode = code(),
            errorDetails = errorBody()?.string()
        )
    }
}
