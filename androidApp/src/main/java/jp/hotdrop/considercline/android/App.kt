package jp.hotdrop.considercline.android

import android.app.Application
import jp.hotdrop.considercline.android.ui.start.splashModule
import jp.hotdrop.considercline.di.databaseModule
import jp.hotdrop.considercline.di.networkModule
import jp.hotdrop.considercline.di.repositoryModule
import jp.hotdrop.considercline.di.settingModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    settingModule,
                    splashModule
                )
            )
        }
    }
}
