import Foundation
import shared

class IosPlatformDependencies: PlatformDependencies {
    let driverFactory: DriverFactory
    let sharedPrefs: KmpSharedPreferences

    init() {
        driverFactory = DriverFactory()
        sharedPrefs = IosKmpSharedPreferences()
    }
}

class IosKmpSharedPreferences: KmpSharedPreferences {
    private let userDefaults = UserDefaults.standard

    func getUserId() async -> String? {
        return userDefaults.string(forKey: "userId")
    }

    func saveUserId(newVal: String) async {
        userDefaults.set(newVal, forKey: "userId")
    }

    func getNickName() async -> String? {
        return userDefaults.string(forKey: "nickName")
    }

    func saveNickName(newVal: String) async {
        userDefaults.set(newVal, forKey: "nickName")
    }

    func getEmail() async -> String? {
        return userDefaults.string(forKey: "email")
    }

    func saveEmail(newVal: String) async {
        userDefaults.set(newVal, forKey: "email")
    }

    func getPoint() async -> Int {
        return userDefaults.integer(forKey: "point")
    }

    func savePoint(newVal: Int) async {
        userDefaults.set(newVal, forKey: "point")
    }
}
