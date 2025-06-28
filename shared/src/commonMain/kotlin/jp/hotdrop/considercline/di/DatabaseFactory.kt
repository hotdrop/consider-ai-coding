package jp.hotdrop.considercline.di

import jp.hotdrop.considercline.db.ConsiderClineDatabase
import jp.hotdrop.considercline.db.DriverFactory
import jp.hotdrop.considercline.repository.local.HistoryDao
import jp.hotdrop.considercline.repository.local.KmpSharedPreferences
import jp.hotdrop.considercline.repository.local.SettingDao

internal interface DatabaseFactory {
    val historyDao: HistoryDao
    val settingDao: SettingDao
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
    override val settingDao: SettingDao by lazy { SettingDao(sharedPreferences) }
}