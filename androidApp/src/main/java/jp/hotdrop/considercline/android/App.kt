package jp.hotdrop.considercline.android

import android.app.Application
import jp.hotdrop.considercline.android.ui.splashModule
import jp.hotdrop.considercline.di.androidModule
import jp.hotdrop.considercline.di.databaseModule
import jp.hotdrop.considercline.di.networkModule
import jp.hotdrop.considercline.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin()
    }

    private fun startKoin() {
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                listOf(
                    androidModule,
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    splashModule
                )
            )
        }
    }
}
