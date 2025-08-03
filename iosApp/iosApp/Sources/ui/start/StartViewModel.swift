import Foundation
import Combine
import shared

class StartViewModel: ObservableObject {
    @Published var viewState: StartViewState = .idle
    @Published var errorAlertItem: ErrorAlertItem?

    private let appSettingUseCase: AppSettingUseCaseProtocol

    init(appSettingUseCase: AppSettingUseCaseProtocol = KmpFactory.shared.useCaseFactory.appSettingUseCase) {
        self.appSettingUseCase = appSettingUseCase
    }

    @MainActor
    func registerUser(nickname: String, email: String) async {
        viewState = .loading
        
        do {
            try await appSettingUseCase.registerUser(nickname: nickname, email: email)
            viewState = .success
        } catch {
            viewState = .idle
            errorAlertItem = ErrorAlertItem(message: error.localizedDescription)
        }
    }
}

// Mock
extension StartViewModel {
    static func mock(_ state: StartViewState, error: ErrorAlertItem? = nil) -> StartViewModel {
        let vm = StartViewModel(appSettingUseCase: DummyAppSettingUseCase())
        vm.viewState = state
        vm.errorAlertItem = error
        return vm
    }
}

struct ErrorAlertItem: Identifiable, Equatable {
    let id = UUID()
    let message: String
}

enum StartViewState: Equatable {
    case idle
    case loading
    case success
}
