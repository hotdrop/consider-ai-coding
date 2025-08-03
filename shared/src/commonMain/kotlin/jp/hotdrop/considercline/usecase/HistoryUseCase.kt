package jp.hotdrop.considercline.usecase

import jp.hotdrop.considercline.model.PointHistory
import jp.hotdrop.considercline.repository.HistoryRepository

class HistoryUseCase(
    private val repository: HistoryRepository
) {
    suspend fun findAll(): Result<List<PointHistory>> {
        return kotlin.runCatching { repository.findAll() }
    }
}