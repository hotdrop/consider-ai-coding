import shared

class IosPlatformDependencies: PlatformDependencies {
    let driverFactory: DriverFactory
    let sharedPrefs: KmpSharedPreferences
    let httpClientState: HttpClientState

    init() {
        driverFactory = DriverFactory()
        sharedPrefs = IosUserDefaultsStore()
        httpClientState = HttpClientState.useFakeHttp()
    }
}
