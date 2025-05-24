package jp.hotdrop.considercline.usecase

import jp.hotdrop.considercline.model.Point
import jp.hotdrop.considercline.repository.PointRepository

class PointUseCase(
    private val repository: PointRepository
) {
    suspend fun find(): Point {
        return repository.find()
    }

    suspend fun acquire(inputPoint: Int) {
        repository.acquire(inputPoint)
    }

    suspend fun use(inputPoint: Int) {
        repository.use(inputPoint)
    }
}