package com.mubarak.thmanyah.core.network.adapter

import com.mubarak.thmanyah.core.common.result.NetworkResult
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(apiCall: suspend () -> T): NetworkResult<T> = try {
    NetworkResult.Success(apiCall())
} catch (e: HttpException) {
    NetworkResult.HttpError(e.code(), e, e.response()?.errorBody()?.string())
} catch (e: IOException) {
    NetworkResult.NetworkError(e)
} catch (e: Exception) {
    NetworkResult.UnknownError(e)
}
