package jp.hotdrop.considercline.di

import io.ktor.client.HttpClient as KtorNativeClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import jp.hotdrop.considercline.db.ConsiderClineDatabase
import jp.hotdrop.considercline.db.DriverFactory
import jp.hotdrop.considercline.repository.AppSettingRepository
import jp.hotdrop.considercline.repository.HistoryRepository
import jp.hotdrop.considercline.repository.PointRepository
import jp.hotdrop.considercline.repository.local.HistoryDao
import jp.hotdrop.considercline.repository.local.KmpSharedPreferences
import jp.hotdrop.considercline.repository.local.SettingDao
import jp.hotdrop.considercline.repository.remote.FakeHttpClient
import jp.hotdrop.considercline.repository.remote.HttpClient
import jp.hotdrop.considercline.repository.remote.KtorHttpClient
import jp.hotdrop.considercline.repository.remote.api.PointApi
import jp.hotdrop.considercline.repository.remote.api.UserApi
import jp.hotdrop.considercline.usecase.AppSettingUseCase
import jp.hotdrop.considercline.usecase.HistoryUseCase
import jp.hotdrop.considercline.usecase.PointUseCase
import kotlinx.serialization.json.Json

interface PlatformDependencies {
    val driverFactory: DriverFactory
    val sharedPrefs: KmpSharedPreferences
}

object KmpUseCaseFactory {
    private lateinit var driverFactory: DriverFactory
    private lateinit var sharedPreferences: KmpSharedPreferences

    fun init(pd: PlatformDependencies) {
        driverFactory = pd.driverFactory
        sharedPreferences = pd.sharedPrefs
    }

    // UserCase
    val appSettingUseCase: AppSettingUseCase by lazy { AppSettingUseCase(appSettingRepository) }
    val historyUseCase: HistoryUseCase by lazy { HistoryUseCase(historyRepository) }
    val pointUseCase: PointUseCase by lazy { PointUseCase(pointRepository) }

    // Repositories
    private val appSettingRepository: AppSettingRepository by lazy { AppSettingRepository(settingDao, userApi) }
    private val historyRepository: HistoryRepository by lazy { HistoryRepository(historyDao) }
    private val pointRepository: PointRepository by lazy { PointRepository(settingDao, pointApi) }

    // HttpClient
    // KtorのHttpClientEngineはCIOを使用。よりプラットフォームに適したエンジン (Android: OkHttp, iOS: Darwin) を
    // expect/actual やネイティブからの注入で設定することも可能
    private val ktorNativeClient: KtorNativeClient by lazy {
        KtorNativeClient(CIO) { // KoinのNetworkModuleの設定を踏襲
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL // TODO: デバッグビルドとリリースビルドでログレベルを分ける
            }
            // 必要に応じてタイムアウト設定などを追加
            // install(HttpTimeout) {
            //     requestTimeoutMillis = 15000L
            //     connectTimeoutMillis = 15000L
            //     socketTimeoutMillis = 15000L
            // }
        }
    }

    // API
//    private val httpClient: HttpClient by lazy { KtorHttpClient(ktorNativeClient) }
    private val httpClient: HttpClient by lazy { FakeHttpClient(sharedPreferences) }
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