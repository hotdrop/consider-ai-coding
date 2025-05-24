package jp.hotdrop.considercline.usecase

import jp.hotdrop.considercline.model.History
import jp.hotdrop.considercline.repository.HistoryRepository

class HistoryUseCase(
    private val repository: HistoryRepository
) {
    suspend fun findAll(): List<History> {
        return repository.findAll()
    }

    suspend fun saveAcquire(value: Int) {
        repository.saveAcquire(value)
    }

    suspend fun saveUse(value: Int) {
        repository.saveUse(value)
    }
}