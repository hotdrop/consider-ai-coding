package jp.hotdrop.considercline.repository

import jp.hotdrop.considercline.model.History
import jp.hotdrop.considercline.repository.local.HistoryDao

class HistoryRepository(
    private val historyDao: HistoryDao
) {
    suspend fun findAll(): List<History> {
        return historyDao.findAll().sortedByDescending { it.dateTime }
    }

    suspend fun saveAcquire(value: Int) {
        historyDao.save(value, "ポイント獲得")
    }

    suspend fun saveUse(value: Int) {
        historyDao.save(-value, "ポイント使用")
    }
}
