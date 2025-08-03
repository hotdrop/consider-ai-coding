package jp.hotdrop.considercline.model

sealed class AppResult<out T> {
    data class Success<T>(val data: T) : AppResult<T>()
    data class Error(val exception: Exception) : AppResult<Nothing>()
}

sealed class AppComplete {
    data object Complete : AppComplete()
    data class Error(val exception: Exception) : AppComplete()
}
