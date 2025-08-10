package jp.hotdrop.considercline.usecase

import jp.hotdrop.considercline.model.AppComplete
import jp.hotdrop.considercline.model.AppResult
import jp.hotdrop.considercline.model.Point
import jp.hotdrop.considercline.model.flatMap
import jp.hotdrop.considercline.model.mapToDomain
import jp.hotdrop.considercline.repository.HistoryRepository
import jp.hotdrop.considercline.repository.PointRepository

class PointUseCase(
    private val pointRepository: PointRepository,
    private val historyRepository: HistoryRepository
) {
    suspend fun find(): AppResult<Point> {
        return fetchPoint()
    }

    suspend fun findForIos(): Point {
        return when(val result = fetchPoint()) {
            is AppResult.Success -> result.data
            is AppResult.Error -> throw mapToDomain(result.error)
        }
    }

    private suspend fun fetchPoint(): AppResult<Point> {
        return pointRepository.find()
    }

    suspend fun acquire(inputPoint: Int): AppComplete {
        return acquireToRepo(inputPoint)
    }

    suspend fun acquireForIos(inputPoint: Int) {
        return when(val result = acquireToRepo(inputPoint)) {
            is AppComplete.Complete -> Unit
            is AppComplete.Error -> throw mapToDomain(result.error)
        }
    }

    private suspend fun acquireToRepo(inputPoint: Int): AppComplete {
        return pointRepository.acquire(inputPoint).flatMap {
            historyRepository.saveAcquire(inputPoint)
        }
    }

    suspend fun use(inputPoint: Int): AppComplete {
        return useToRepo(inputPoint)
    }

    suspend fun useForIos(inputPoint: Int) {
        return when(val result = useToRepo(inputPoint)) {
            is AppComplete.Complete -> Unit
            is AppComplete.Error -> throw mapToDomain(result.error)
        }
    }

    private suspend fun useToRepo(inputPoint: Int): AppComplete {
        return pointRepository.use(inputPoint).flatMap {
            historyRepository.saveUse(inputPoint)
        }
    }
}