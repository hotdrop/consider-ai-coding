package jp.hotdrop.considercline.usecase

import jp.hotdrop.considercline.model.AppComplete
import jp.hotdrop.considercline.model.AppError
import jp.hotdrop.considercline.model.AppResult
import jp.hotdrop.considercline.model.Point
import jp.hotdrop.considercline.model.flatMap
import jp.hotdrop.considercline.repository.HistoryRepository
import jp.hotdrop.considercline.repository.PointRepository
import kotlin.coroutines.cancellation.CancellationException

class PointUseCase(
    private val pointRepository: PointRepository,
    private val historyRepository: HistoryRepository
) {
    suspend fun find(): AppResult<Point> {
        return runCatching {
            pointRepository.find()
        }.getOrElse { throwable ->
            if (throwable is CancellationException) {
                throw throwable
            }
            AppResult.Error(AppError.UnknownError(throwable))
        }
    }

    suspend fun acquire(inputPoint: Int): AppComplete {
        return runCatching {
            pointRepository.acquire(inputPoint).flatMap {
                historyRepository.saveAcquire(inputPoint)
            }
        }.getOrElse { throwable ->
            if (throwable is CancellationException) {
                throw throwable
            }
            AppComplete.Error(AppError.UnknownError(throwable))
        }
    }

    suspend fun use(inputPoint: Int): AppComplete {
        return runCatching {
            pointRepository.use(inputPoint).flatMap {
                historyRepository.saveUse(inputPoint)
            }
        }.getOrElse { throwable ->
            if (throwable is CancellationException) {
                throw throwable
            }
            AppComplete.Error(AppError.UnknownError(throwable))
        }
    }
}