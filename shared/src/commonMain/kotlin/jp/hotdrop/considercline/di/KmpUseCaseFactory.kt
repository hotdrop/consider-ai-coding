package jp.hotdrop.considercline.di

import jp.hotdrop.considercline.repository.remote.HttpClientFactory
import jp.hotdrop.considercline.db.ConsiderClineDatabase
import jp.hotdrop.considercline.db.DriverFactory
import jp.hotdrop.considercline.model.HttpClientState
import jp.hotdrop.considercline.repository.AppSettingRepository
import jp.hotdrop.considercline.repository.HistoryRepository
import jp.hotdrop.considercline.repository.PointRepository
import jp.hotdrop.considercline.repository.local.HistoryDao
import jp.hotdrop.considercline.repository.local.KmpSharedPreferences
import jp.hotdrop.considercline.repository.local.SettingDao
import jp.hotdrop.considercline.repository.remote.HttpClient
import jp.hotdrop.considercline.repository.remote.api.PointApi
import jp.hotdrop.considercline.repository.remote.api.UserApi
import jp.hotdrop.considercline.usecase.AppSettingUseCase
import jp.hotdrop.considercline.usecase.HistoryUseCase
import jp.hotdrop.considercline.usecase.PointUseCase

interface PlatformDependencies {
    val driverFactory: DriverFactory
    val sharedPrefs: KmpSharedPreferences
    val httpClientState: HttpClientState
}

object KmpUseCaseFactory {
    private lateinit var driverFactory: DriverFactory
    private lateinit var sharedPreferences: KmpSharedPreferences
    private lateinit var httpClientState: HttpClientState

    fun init(pd: PlatformDependencies) {
        driverFactory = pd.driverFactory
        sharedPreferences = pd.sharedPrefs
        httpClientState = pd.httpClientState
    }

    // UserCase
    val appSettingUseCase: AppSettingUseCase by lazy { AppSettingUseCase(appSettingRepository) }
    val historyUseCase: HistoryUseCase by lazy { HistoryUseCase(historyRepository) }
    val pointUseCase: PointUseCase by lazy { PointUseCase(pointRepository, historyRepository) }

    // Repositories
    private val appSettingRepository: AppSettingRepository by lazy { AppSettingRepository(settingDao, userApi) }
    private val historyRepository: HistoryRepository by lazy { HistoryRepository(historyDao) }
    private val pointRepository: PointRepository by lazy { PointRepository(settingDao, pointApi) }

    // API
    private val httpClient: HttpClient by lazy { HttpClientFactory.create(httpClientState, sharedPreferences) }
    private val pointApi: PointApi by lazy { PointApi(httpClient) }
    private val userApi: UserApi by lazy { UserApi(httpClient) }

    // Database
    private val database: ConsiderClineDatabase by lazy {
        val driver = driverFactory.createDriver()
        ConsiderClineDatabase(driver)
    }
    private val historyDao: HistoryDao by lazy { HistoryDao(database) }
    private val settingDao: SettingDao by lazy { SettingDao(sharedPreferences) }
}