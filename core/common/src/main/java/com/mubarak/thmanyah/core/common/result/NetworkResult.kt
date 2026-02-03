package com.mubarak.thmanyah.core.common.result

sealed interface NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>

    sealed interface Failure : NetworkResult<Nothing> {
        val throwable: Throwable
        val message: String get() = throwable.localizedMessage ?: "Unknown error"
    }

    data class HttpError(val code: Int, override val throwable: Throwable, val body: String? = null) : Failure {
        override val message: String get() = when (code) {
            400 -> "Bad request"
            401 -> "Unauthorized"
            403 -> "Forbidden"
            404 -> "Not found"
            500 -> "Internal server error"
            else -> "HTTP Error: $code"
        }
    }

    data class NetworkError(override val throwable: Throwable) : Failure {
        override val message: String get() = "Network connection error"
    }

    data class UnknownError(override val throwable: Throwable) : Failure

    fun getOrNull(): T? = (this as? Success)?.data

    fun toResource(): Resource<T> = when (this) {
        is Success -> Resource.success(data)
        is Failure -> Resource.error(throwable)
    }
}
