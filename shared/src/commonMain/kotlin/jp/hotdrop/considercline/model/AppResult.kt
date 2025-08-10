package jp.hotdrop.considercline.model


/**
 * Android用のResultクラス
 */
sealed class AppResult<out T> {
    data class Success<T>(val data: T) : AppResult<T>()
    data class Error(val error: AppError) : AppResult<Nothing>()
}

/**
 * Android用のCompleteクラス
 */
sealed class AppComplete {
    data object Complete : AppComplete()
    data class Error(val error: AppError) : AppComplete()
}

inline fun <T, R> AppResult<T>.map(transform: (T) -> R): AppResult<R> = when (this) {
    is AppResult.Success -> AppResult.Success(transform(data))
    is AppResult.Error   -> this
}

inline fun <T, R> AppResult<T>.flatMap(transform: (T) -> AppResult<R>): AppResult<R> = when (this) {
    is AppResult.Success -> transform(data)
    is AppResult.Error   -> this
}

inline fun <T> AppResult<T>.fold(onSuccess: (T) -> Unit, onFailure: (AppError) -> Unit) {
    when (this) {
        is AppResult.Success -> onSuccess(data)
        is AppResult.Error   -> onFailure(error)
    }
}

inline fun AppComplete.flatMap(transform: () -> AppComplete): AppComplete = when (this) {
    is AppComplete.Complete -> transform()
    is AppComplete.Error   -> this
}
