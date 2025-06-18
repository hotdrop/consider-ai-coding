import shared

class IosPlatformDependencies: PlatformDependencies {
    let driverFactory: DriverFactory
    let sharedPrefs: KmpSharedPreferences

    init() {
        driverFactory = DriverFactory()
        sharedPrefs = IosUserDefaultsStore()
    }
}
