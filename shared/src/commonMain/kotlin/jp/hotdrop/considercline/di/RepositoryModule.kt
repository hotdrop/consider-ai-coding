package jp.hotdrop.considercline.di

import jp.hotdrop.considercline.repository.AppSettingRepository
import jp.hotdrop.considercline.repository.HistoryRepository
import jp.hotdrop.considercline.repository.PointRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { AppSettingRepository(get(), get()) }
    single { HistoryRepository(get()) }
    single { PointRepository(get(), get()) }
}
