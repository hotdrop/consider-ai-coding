package jp.hotdrop.considercline.di

import jp.hotdrop.considercline.db.DriverFactory
import jp.hotdrop.considercline.model.HttpClientState
import jp.hotdrop.considercline.repository.local.KmpSharedPreferences

interface PlatformDependencies {
    val driverFactory: DriverFactory
    val sharedPrefs: KmpSharedPreferences
    val httpClientState: HttpClientState
}

object KmpFactory {
    lateinit var useCaseFactory: UseCaseFactory
        private set

    fun init(pd: PlatformDependencies) {
        val databaseFactory: DatabaseFactory = DatabaseFactoryImpl(pd.driverFactory, pd.sharedPrefs)
        val apiFactory: ApiFactory = ApiFactoryImpl(pd.httpClientState, pd.sharedPrefs)
        val repositoryFactory: RepositoryFactory = RepositoryFactoryImpl(databaseFactory, apiFactory)
        useCaseFactory = UseCaseFactoryImpl(repositoryFactory)
    }
}