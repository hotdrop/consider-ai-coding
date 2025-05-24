package jp.hotdrop.considercline

import android.content.Context
import jp.hotdrop.considercline.db.DriverFactory
import jp.hotdrop.considercline.di.PlatformDependencies
import jp.hotdrop.considercline.repository.local.KmpSharedPreferences

/**
 *
 */
class AndroidPlatformDependencies(context: Context) : PlatformDependencies {
    override val driverFactory: DriverFactory by lazy { DriverFactory(context)}
    override val sharedPrefs: KmpSharedPreferences by lazy { KmpSharedPreferences() }
}