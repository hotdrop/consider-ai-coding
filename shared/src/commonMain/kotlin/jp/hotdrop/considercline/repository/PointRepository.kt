package jp.hotdrop.considercline.repository

import jp.hotdrop.considercline.model.AppComplete
import jp.hotdrop.considercline.model.AppResult
import jp.hotdrop.considercline.model.Point
import jp.hotdrop.considercline.repository.local.UserDao
import jp.hotdrop.considercline.repository.remote.api.PointApi

class PointRepository(
    private val userDao: UserDao,
    private val pointApi: PointApi
) : BaseRepository() {
    suspend fun find(): AppResult<Point> {
        return execWithResult {
            val userId = requireNotNull(userDao.getUserId()) { "User ID must not be null" }
            pointApi.find(userId)
        }
    }

    suspend fun acquire(inputPoint: Int): AppComplete {
        return execWithComplete {
            val userId = requireNotNull(userDao.getUserId()) { "User ID must not be null" }
            pointApi.acquired(userId, inputPoint)
            AppComplete.Complete
        }
    }

    suspend fun use(inputPoint: Int): AppComplete {
        return execWithComplete {
            val userId = requireNotNull(userDao.getUserId()) { "User ID must not be null" }
            pointApi.use(userId, inputPoint)
            AppComplete.Complete
        }
    }
}
