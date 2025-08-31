package jp.hotdrop.considercline.repository

import jp.hotdrop.considercline.model.AppComplete
import jp.hotdrop.considercline.model.AppResult
import jp.hotdrop.considercline.model.PointHistory
import jp.hotdrop.considercline.repository.local.HistoryDao

class HistoryRepository(
    private val historyDao: HistoryDao
) : BaseRepository() {
    suspend fun findAll(): AppResult<List<PointHistory>> {
        return execWithResult {
            historyDao.findAll()
        }
    }

    suspend fun saveAcquire(value: Int): AppComplete {
        return execWithComplete {
            historyDao.save(value, "ポイント獲得")
            AppComplete.Complete
        }
    }

    suspend fun saveUse(value: Int): AppComplete {
        return execWithComplete {
            historyDao.save(-value, "ポイント使用")
            AppComplete.Complete
        }
    }
}
