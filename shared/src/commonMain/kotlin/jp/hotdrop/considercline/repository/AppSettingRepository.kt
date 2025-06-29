package jp.hotdrop.considercline.repository

import jp.hotdrop.considercline.model.AppSetting
import jp.hotdrop.considercline.repository.local.KmpSharedPreferences
import jp.hotdrop.considercline.repository.local.SettingDao
import jp.hotdrop.considercline.repository.remote.api.UserApi

class AppSettingRepository(
    private val settingDao: SettingDao,
    private val userApi: UserApi,
    private val kmpSharedPreferences: KmpSharedPreferences
) {
    suspend fun find(): AppSetting {
        return AppSetting(
            userId = settingDao.getUserId(),
            nickName = settingDao.getNickName(),
            email = settingDao.getEmail()
        )
    }

    suspend fun registerUser(nickname: String?, email: String?) {
        val userResponse = userApi.create(nickname, email)
        settingDao.save(userId = userResponse.userId, nickName = nickname, email = email)
        kmpSharedPreferences.saveJwt(userResponse.jwt)
        kmpSharedPreferences.saveRefreshToken(userResponse.refreshToken)
    }
}
