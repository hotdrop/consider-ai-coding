import Foundation
import Combine
import shared

enum ViewState {
    case idle
    case loading
    case success
    case error(String)
}

class StartViewModel: ObservableObject {
    @Published var viewState: ViewState = .idle

    private let appSettingUseCase: AppSettingUseCaseProtocol

    init(appSettingUseCase: AppSettingUseCaseProtocol = KmpUseCaseFactory().appSettingUseCase) {
        self.appSettingUseCase = appSettingUseCase
    }

    func registerUser(nickname: String, email: String) async {
        do {
            await MainActor.run {
                viewState = .loading
            }
            
            try await appSettingUseCase.registerUser(nickname: nickname, email: email)
            await MainActor.run {
                viewState = .success
            }
        } catch {
            await MainActor.run {
                viewState = .error(error.localizedDescription)
            }
        }
    }
}

// Mock
extension StartViewModel {
    static func mock(_ state: ViewState) -> StartViewModel {
        let vm = StartViewModel(appSettingUseCase: DummyAppSettingUseCase())
        vm.viewState = state
        return vm
    }
}

class DummyAppSettingForStartUseCase: AppSettingUseCaseProtocol {
    func find() async throws -> AppSetting {
        return AppSetting(userId: "dummy", nickName: "dummy", email: "dummy@example.com")
    }

    func registerUser(nickname: String?, email: String?) async throws {
        
    }
}
