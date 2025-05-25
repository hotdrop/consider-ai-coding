package jp.hotdrop.considercline.usecase

import jp.hotdrop.considercline.model.Point
import jp.hotdrop.considercline.repository.HistoryRepository
import jp.hotdrop.considercline.repository.PointRepository

class PointUseCase(
    private val pointRepository: PointRepository,
    private val historyRepository: HistoryRepository
) {
    suspend fun find(): Point {
        return pointRepository.find()
    }

    suspend fun acquire(inputPoint: Int) {
        pointRepository.acquire(inputPoint)
        historyRepository.saveAcquire(inputPoint)
    }

    suspend fun use(inputPoint: Int) {
        pointRepository.use(inputPoint)
        historyRepository.saveUse(inputPoint)
    }
}