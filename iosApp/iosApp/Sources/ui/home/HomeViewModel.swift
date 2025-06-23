import Foundation
import Combine
import shared

struct HomeViewState {
    let nickname: String
    let email: String
    let point: Int
    let histories: [History]
}

enum HomeViewError: Error {
    case appSettingNotInitialized
    case unknown(String)
}

class HomeViewModel: ObservableObject {
    @Published var viewState: HomeViewState? = nil
    @Published var error: HomeViewError? = nil

    private let appSettingUseCase: AppSettingUseCaseProtocol
    private let pointUseCase: PointUseCaseProtocol
    private let historyUseCase: HistoryUseCaseProtocol

    init(appSettingUseCase: AppSettingUseCaseProtocol = KmpUseCaseFactory.shared.appSettingUseCase,
         pointUseCase: PointUseCaseProtocol = KmpUseCaseFactory.shared.pointUseCase,
         historyUseCase: HistoryUseCaseProtocol = KmpUseCaseFactory.shared.historyUseCase) {
        self.appSettingUseCase = appSettingUseCase
        self.pointUseCase = pointUseCase
        self.historyUseCase = historyUseCase
    }

    @MainActor
    func load() async {
        if Task.isCancelled { return }
        
        do {
            viewState = nil
            error = nil
            
            let appSetting = try await appSettingUseCase.find()
            let point = try await pointUseCase.find()
            let histories = try await historyUseCase.findAll()

            if appSetting.isInitialized(), let nickname = appSetting.nickName, let email = appSetting.email {
                self.viewState = HomeViewState(nickname: nickname, email: email, point: Int(point.point), histories: histories)
            } else {
                self.error = .appSettingNotInitialized
            }
        } catch {
            self.error = .unknown(error.localizedDescription)
        }
    }
}

// Mock
extension HomeViewModel {
    static func mock(_ state: HomeViewState? = nil, error: HomeViewError? = nil) -> HomeViewModel {
        let vm = HomeViewModel(appSettingUseCase: DummyAppSettingUseCase())
        vm.viewState = state
        vm.error = error
        return vm
    }
}