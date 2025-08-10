package jp.hotdrop.considercline.di

import jp.hotdrop.considercline.db.ConsiderClineDatabase
import jp.hotdrop.considercline.db.DriverFactory
import jp.hotdrop.considercline.repository.local.HistoryDao
import jp.hotdrop.considercline.repository.local.KmpSharedPreferences
import jp.hotdrop.considercline.repository.local.UserDao

internal interface DatabaseFactory {
    val historyDao: HistoryDao
    val userDao: UserDao
}

internal class DatabaseFactoryImpl(
    driverFactory: DriverFactory,
    sharedPreferences: KmpSharedPreferences
): DatabaseFactory {

    private val database: ConsiderClineDatabase by lazy {
        val driver = driverFactory.createDriver()
        ConsiderClineDatabase(driver)
    }

    override val historyDao: HistoryDao by lazy { HistoryDao(database) }
    override val userDao: UserDao by lazy { UserDao(sharedPreferences) }
}