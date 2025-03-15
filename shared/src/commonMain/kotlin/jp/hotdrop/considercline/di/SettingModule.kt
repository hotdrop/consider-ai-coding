package jp.hotdrop.considercline.di

import jp.hotdrop.considercline.repository.local.SettingDao
import jp.hotdrop.considercline.repository.local.SettingDaoImpl
import org.koin.dsl.module

val settingModule = module {
    single<SettingDao> { SettingDaoImpl(get()) }
}
