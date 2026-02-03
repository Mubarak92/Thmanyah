package com.mubarak.thmanyah.core.common.result

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val exception: Throwable, val message: String = exception.localizedMessage ?: "Unknown error") : Resource<Nothing>()
    data object Loading : Resource<Nothing>()
    data object Empty : Resource<Nothing>()

    val isSuccess get() = this is Success
    val isError get() = this is Error
    val isLoading get() = this is Loading

    fun getOrNull(): T? = (this as? Success)?.data

    inline fun <R> map(transform: (T) -> R): Resource<R> = when (this) {
        is Success -> Success(transform(data))
        is Error -> this
        is Loading -> Loading
        is Empty -> Empty
    }

    inline fun onSuccess(action: (T) -> Unit): Resource<T> {
        if (this is Success) action(data)
        return this
    }

    inline fun onError(action: (Throwable, String) -> Unit): Resource<T> {
        if (this is Error) action(exception, message)
        return this
    }

    companion object {
        fun <T> success(data: T): Resource<T> = Success(data)
        fun error(exception: Throwable): Resource<Nothing> = Error(exception)
        fun error(message: String): Resource<Nothing> = Error(Exception(message), message)
        fun loading(): Resource<Nothing> = Loading
        fun empty(): Resource<Nothing> = Empty
    }
}
