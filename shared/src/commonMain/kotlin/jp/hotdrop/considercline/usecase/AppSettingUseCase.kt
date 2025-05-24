package jp.hotdrop.considercline.usecase

import jp.hotdrop.considercline.model.AppSetting
import jp.hotdrop.considercline.repository.AppSettingRepository

class AppSettingUseCase(
    private val appSettingRepository: AppSettingRepository
) {
    suspend fun find(): AppSetting {
        return appSettingRepository.find()
    }

    suspend fun registerUser(nickname: String?, email: String?) {
        appSettingRepository.registerUser(nickname, email)
    }
}