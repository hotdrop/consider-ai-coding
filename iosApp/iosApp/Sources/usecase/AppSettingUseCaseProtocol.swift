import shared

protocol AppSettingUseCaseProtocol {
    func find() async throws -> AppSetting
    func registerUser(nickname: String?, email: String?) async throws
}

extension AppSettingUseCase: AppSettingUseCaseProtocol {}

class DummyAppSettingUseCase: AppSettingUseCaseProtocol {
    func find() async throws -> AppSetting {
        let appSetting = AppSetting(userId: "preview001", nickName: "preview name", email: "preview@email.com")
        return appSetting
    }
    func registerUser(nickname: String?, email: String?) async throws {}
}
