package jp.hotdrop.considercline.repository

import io.ktor.client.plugins.ResponseException
import jp.hotdrop.considercline.model.AppComplete
import jp.hotdrop.considercline.model.AppError
import jp.hotdrop.considercline.model.AppResult
import kotlin.coroutines.cancellation.CancellationException

abstract class BaseRepository {
    suspend fun <T> execWithResult(block: suspend () -> T): AppResult<T> {
        return try {
            AppResult.Success(block())
        } catch (e: Exception) {
            // Coroutineのキャンセル例外は必ず伝搬する
            if (e is CancellationException) {
                throw e
            }

            val error = when (e) {
                is ResponseException -> AppError.NetworkError(e)
                else -> AppError.UnknownError(e)
            }
            AppResult.Error(error)
        }
    }

    suspend fun execWithComplete(block: suspend () -> AppComplete): AppComplete {
        return try {
            block()
        } catch (e: Exception) {
            // Coroutineのキャンセル例外は必ず伝搬する
            if (e is CancellationException) {
                throw e
            }

            val error = when (e) {
                is ResponseException -> AppError.NetworkError(e)
                else -> AppError.UnknownError(e)
            }
            AppComplete.Error(error)
        }
    }
}