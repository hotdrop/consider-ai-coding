package jp.hotdrop.considercline.repository

import jp.hotdrop.considercline.model.Point
import jp.hotdrop.considercline.repository.local.UserDao
import jp.hotdrop.considercline.repository.remote.api.PointApi

class PointRepository(
    private val userDao: UserDao,
    private val pointApi: PointApi
) {
    suspend fun find(): Point {
        val userId = requireNotNull(userDao.getUserId()) { "User ID must not be null" }
        return pointApi.find(userId)
    }

    suspend fun acquire(inputPoint: Int) {
        val userId = requireNotNull(userDao.getUserId()) { "User ID must not be null" }
        pointApi.acquired(userId, inputPoint)
    }

    suspend fun use(inputPoint: Int) {
        val userId = requireNotNull(userDao.getUserId()) { "User ID must not be null" }
        pointApi.use(userId, inputPoint)
    }
}
