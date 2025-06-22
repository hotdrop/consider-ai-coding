import Foundation
import Combine
import shared

enum MainViewState {
    case loading
    case loaded(String) // userId
    case firstTime
    case error(String)
}

class MainViewModel: ObservableObject {
    @Published var viewState: MainViewState = .loading

    private let useCase: AppSettingUseCaseProtocol

    init(appSettingUseCase: AppSettingUseCaseProtocol = KmpUseCaseFactory.shared.appSettingUseCase) {
        self.useCase = appSettingUseCase
    }

    func load() async {
        viewState = .loading
        do {
            let appSetting = try await useCase.find()
            await MainActor.run {
                if appSetting.isInitialized(), let userId = appSetting.userId {
                    self.viewState = .loaded(userId)
                } else if appSetting.isInitialized() {
                    self.viewState = .error("User ID is missing despite being initialized.")
                } else {
                    self.viewState = .firstTime
                }
            }
        } catch {
            await MainActor.run {
                self.viewState = .error(error.localizedDescription)
            }
        }
    }
}

// Mock
extension MainViewModel {
    static func mock(_ state: MainViewState) -> MainViewModel {
        let vm = MainViewModel(appSettingUseCase: DummyAppSettingUseCase())
        vm.viewState = state
        return vm
    }
}

class DummyAppSettingUseCase: AppSettingUseCaseProtocol {
    func find() async throws -> AppSetting {
        let appSetting = AppSetting(userId: "preview001", nickName: "preview name", email: "preview@email.com")
        return appSetting
    }
}
