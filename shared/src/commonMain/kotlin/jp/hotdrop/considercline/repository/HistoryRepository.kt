package jp.hotdrop.considercline.repository

import jp.hotdrop.considercline.model.AppResult
import jp.hotdrop.considercline.model.PointHistory
import jp.hotdrop.considercline.repository.local.HistoryDao

class HistoryRepository(
    private val historyDao: HistoryDao
) {
    suspend fun findAll(): AppResult<List<PointHistory>> {
        try {
            val histories = historyDao.findAll()
            return AppResult.Success(histories)
        } catch (e: Exception) {
            return AppResult.Error(e)
        }
    }

    suspend fun saveAcquire(value: Int) {
        historyDao.save(value, "ポイント獲得")
    }

    suspend fun saveUse(value: Int) {
        historyDao.save(-value, "ポイント使用")
    }
}
