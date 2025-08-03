import Foundation
import Combine
import shared

class MainViewModel: ObservableObject {
    @Published var viewState: MainViewState = .loading

    private let useCase: AppSettingUseCaseProtocol
    private let loadAction: (() async -> Void)?

    init(
        appSettingUseCase: AppSettingUseCaseProtocol = KmpFactory.shared.useCaseFactory.appSettingUseCase,
        loadAction: (() async -> Void)? = nil
    ) {
        self.useCase = appSettingUseCase
        self.loadAction = loadAction
    }

    @MainActor
    func load() async {
        if Task.isCancelled { return }
        if let customLoad = loadAction {
            await customLoad()
            return
        }
        
        do {
            viewState = .loading
            
            let appSetting = try await useCase.find()
            if appSetting.isInitialized(), let userId = appSetting.userId {
                self.viewState = .loaded(userId)
            } else if appSetting.isInitialized() {
                self.viewState = .error("User ID is missing despite being initialized.")
            } else {
                self.viewState = .firstTime
            }
        } catch {
            self.viewState = .error(error.localizedDescription)
        }
    }
}

// Mock
extension MainViewModel {
    static func mock(_ state: MainViewState) -> MainViewModel {
        let vm = MainViewModel(appSettingUseCase: DummyAppSettingUseCase(), loadAction: {})
        vm.viewState = state
        return vm
    }
}

enum MainViewState {
    case loading
    case loaded(String) // userId
    case firstTime
    case error(String)
}
