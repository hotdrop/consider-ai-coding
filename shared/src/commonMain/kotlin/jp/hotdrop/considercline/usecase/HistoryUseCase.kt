package jp.hotdrop.considercline.usecase

import jp.hotdrop.considercline.model.AppError
import jp.hotdrop.considercline.model.AppResult
import jp.hotdrop.considercline.model.PointHistory
import jp.hotdrop.considercline.repository.HistoryRepository
import kotlin.coroutines.cancellation.CancellationException

class HistoryUseCase(
    private val repository: HistoryRepository
) {
    suspend fun findAll(): AppResult<List<PointHistory>> {
        return runCatching {
            repository.findAll()
        }.getOrElse { throwable ->
            if (throwable is CancellationException) {
                throw throwable
            }
            AppResult.Error(AppError.UnknownError(throwable))
        }
    }
}