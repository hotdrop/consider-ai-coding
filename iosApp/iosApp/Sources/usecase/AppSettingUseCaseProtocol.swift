import shared

protocol AppSettingUseCaseProtocol {
    func find() async throws -> AppSetting
    func registerUser(nickname: String?, email: String?) async throws
}

extension AppSettingUseCase: AppSettingUseCaseProtocol {}
