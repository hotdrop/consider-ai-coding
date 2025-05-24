package jp.hotdrop.considercline.android

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import jp.hotdrop.considercline.di.KmpUseCaseFactory

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        KmpUseCaseFactory.init(AndroidPlatformDependencies(this))
    }
}
