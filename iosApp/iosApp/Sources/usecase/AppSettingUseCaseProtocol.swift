import shared

protocol AppSettingUseCaseProtocol {
    func find() async throws -> AppSetting
}

extension AppSettingUseCase: AppSettingUseCaseProtocol {}
