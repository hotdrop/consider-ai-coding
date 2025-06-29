package jp.hotdrop.considercline.di

import jp.hotdrop.considercline.repository.AppSettingRepository
import jp.hotdrop.considercline.repository.HistoryRepository
import jp.hotdrop.considercline.repository.PointRepository
import jp.hotdrop.considercline.repository.local.KmpSharedPreferences

internal interface RepositoryFactory {
    val appSettingRepository: AppSettingRepository
    val historyRepository: HistoryRepository
    val pointRepository: PointRepository
}

internal class RepositoryFactoryImpl(
    private val databaseFactory: DatabaseFactory,
    private val apiFactory: ApiFactory,
    private val kmpSharedPreferences: KmpSharedPreferences
): RepositoryFactory {

    override val appSettingRepository: AppSettingRepository by lazy {
        AppSettingRepository(databaseFactory.settingDao, apiFactory.userApi, kmpSharedPreferences)
    }
    override val historyRepository: HistoryRepository by lazy {
        HistoryRepository(databaseFactory.historyDao)
    }
    override val pointRepository: PointRepository by lazy {
        PointRepository(databaseFactory.settingDao, apiFactory.pointApi)
    }
}