package jp.hotdrop.considercline.repository

import jp.hotdrop.considercline.model.AppComplete
import jp.hotdrop.considercline.model.AppResult
import jp.hotdrop.considercline.model.User
import jp.hotdrop.considercline.repository.local.KmpSharedPreferences
import jp.hotdrop.considercline.repository.local.UserDao
import jp.hotdrop.considercline.repository.remote.api.UserApi

class UserRepository(
    private val userDao: UserDao,
    private val userApi: UserApi,
    private val kmpSharedPreferences: KmpSharedPreferences
) : BaseRepository() {
    suspend fun find(): AppResult<User> {
        return execWithResult {
            User(
                userId = userDao.getUserId(),
                nickName = userDao.getNickName(),
                email = userDao.getEmail()
            )
        }
    }

    suspend fun registerUser(nickname: String?, email: String?): AppComplete {
        return execWithComplete {
            val userResponse = userApi.create(nickname, email)
            userDao.save(userId = userResponse.userId, nickName = nickname, email = email)
            kmpSharedPreferences.saveJwt(userResponse.jwt)
            kmpSharedPreferences.saveRefreshToken(userResponse.refreshToken)
            AppComplete.Complete
        }
    }
}
