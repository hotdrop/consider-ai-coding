package jp.hotdrop.considercline.usecase

import jp.hotdrop.considercline.model.AppResult
import jp.hotdrop.considercline.model.PointHistory
import jp.hotdrop.considercline.model.mapToDomain
import jp.hotdrop.considercline.repository.HistoryRepository

class HistoryUseCase(
    private val repository: HistoryRepository
) {
    suspend fun findAll(): AppResult<List<PointHistory>> {
        return fetchHistories()
    }

    suspend fun findAllForIos(): List<PointHistory> {
        return when(val result = fetchHistories()) {
            is AppResult.Success -> result.data
            is AppResult.Error -> throw mapToDomain(result.error)
        }
    }

    private suspend fun fetchHistories(): AppResult<List<PointHistory>> {
        return repository.findAll()
    }
}