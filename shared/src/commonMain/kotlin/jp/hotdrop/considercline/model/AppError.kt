package jp.hotdrop.considercline.model

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

data class ProgramException(val errorMessage: String): Exception(errorMessage)