package jp.hotdrop.considercline.di

import com.russhwolf.settings.Settings
import jp.hotdrop.considercline.db.DriverFactory
import jp.hotdrop.considercline.db.createDatabase
import jp.hotdrop.considercline.repository.AppSettingRepository
import jp.hotdrop.considercline.repository.local.HistoryDao
import jp.hotdrop.considercline.repository.local.SettingDao
import jp.hotdrop.considercline.repository.local.SettingDaoImpl
import jp.hotdrop.considercline.repository.remote.api.UserApi
import org.koin.dsl.module

val databaseModule = module {
    single { get<DriverFactory>() }
    single { createDatabase(get()) }
    single { HistoryDao() }
    single<SettingDao> { SettingDaoImpl(get<Settings>()) }
    single { AppSettingRepository(get(), get<UserApi>()) }
}
