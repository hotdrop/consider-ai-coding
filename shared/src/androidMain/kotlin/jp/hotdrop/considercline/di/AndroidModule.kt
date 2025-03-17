package jp.hotdrop.considercline.di

import android.content.Context
import com.russhwolf.settings.SharedPreferencesSettings
import com.russhwolf.settings.Settings
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidModule = module {
    single<Settings> {
        SharedPreferencesSettings(
            androidContext().getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        )
    }
}