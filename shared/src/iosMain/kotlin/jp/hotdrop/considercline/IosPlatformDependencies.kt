package jp.hotdrop.considercline

import jp.hotdrop.considercline.db.DriverFactory
import jp.hotdrop.considercline.di.PlatformDependencies
import jp.hotdrop.considercline.repository.local.KmpSharedPreferences

class IosPlatformDependencies: PlatformDependencies {
    override val driverFactory: DriverFactory by lazy { DriverFactory() }
    override val sharedPrefs: KmpSharedPreferences by lazy { KmpSharedPreferences() }
}