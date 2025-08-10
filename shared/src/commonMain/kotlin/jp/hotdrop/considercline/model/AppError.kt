package jp.hotdrop.considercline.model

import kotlin.coroutines.cancellation.CancellationException

sealed class AppError {
    data class NetworkError(val error: Throwable): AppError()
    data class UnknownError(val error: Throwable): AppError()
    data class ProgramError(val errorMessage: String): AppError()

    val message: String
        get() = when (this) {
            is NetworkError -> error.message ?: "Network Error"
            is UnknownError -> error.message ?: "Unknown Error"
            is ProgramError -> errorMessage
        }
}

fun mapToDomain(appError: AppError): Throwable =
    when (appError) {
        is AppError.NetworkError -> appError.error
        is AppError.UnknownError -> appError.error
        is AppError.ProgramError -> ProgramException(appError.message)
    }

fun mapToThrowable(t: Throwable): AppError =
    when (t) {
        is CancellationException -> throw t
        else -> AppError.UnknownError(t)
    }

data class ProgramException(val errorMessage: String): Exception(errorMessage)