package jp.hotdrop.considercline.di

import jp.hotdrop.considercline.db.DriverFactory
import jp.hotdrop.considercline.db.createDatabase
import jp.hotdrop.considercline.repository.local.HistoryDao
import org.koin.dsl.module

val databaseModule = module {
    single { get<DriverFactory>() }
    single { createDatabase(get()) }
    single { HistoryDao() }
}
