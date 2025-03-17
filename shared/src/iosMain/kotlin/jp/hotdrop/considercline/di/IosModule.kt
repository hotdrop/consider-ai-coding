package jp.hotdrop.considercline.di

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

val iosModule = module {
    single<Settings> {
        // NSUserDefaults.standardUserDefaults を用いてSettingsインスタンスを生成
        NSUserDefaultsSettings(NSUserDefaults.standardUserDefaults)
    }
}