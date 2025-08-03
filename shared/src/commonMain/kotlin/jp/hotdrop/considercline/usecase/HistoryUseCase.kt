package jp.hotdrop.considercline.usecase

import jp.hotdrop.considercline.model.AppResult
import jp.hotdrop.considercline.model.PointHistory
import jp.hotdrop.considercline.repository.HistoryRepository

class HistoryUseCase(
    private val repository: HistoryRepository
) {
    suspend fun findAll(): AppResult<List<PointHistory>> {
        return repository.findAll()
    }
}