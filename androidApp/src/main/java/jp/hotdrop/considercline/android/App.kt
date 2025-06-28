package jp.hotdrop.considercline.android

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import jp.hotdrop.considercline.di.KmpFactory

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        KmpFactory.init(AndroidPlatformDependencies(this))
    }
}
