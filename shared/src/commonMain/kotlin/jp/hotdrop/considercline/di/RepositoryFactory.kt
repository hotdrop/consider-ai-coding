package jp.hotdrop.considercline.di

import jp.hotdrop.considercline.repository.UserRepository
import jp.hotdrop.considercline.repository.HistoryRepository
import jp.hotdrop.considercline.repository.PointRepository
import jp.hotdrop.considercline.repository.local.KmpSharedPreferences

internal interface RepositoryFactory {
    val userRepository: UserRepository
    val historyRepository: HistoryRepository
    val pointRepository: PointRepository
}

internal class RepositoryFactoryImpl(
    private val databaseFactory: DatabaseFactory,
    private val apiFactory: ApiFactory,
    private val kmpSharedPreferences: KmpSharedPreferences
): RepositoryFactory {

    override val userRepository: UserRepository by lazy {
        UserRepository(databaseFactory.userDao, apiFactory.userApi, kmpSharedPreferences)
    }
    override val historyRepository: HistoryRepository by lazy {
        HistoryRepository(databaseFactory.historyDao)
    }
    override val pointRepository: PointRepository by lazy {
        PointRepository(databaseFactory.userDao, apiFactory.pointApi)
    }
}