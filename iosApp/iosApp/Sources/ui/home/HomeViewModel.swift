import Foundation
import Combine
import shared

enum HomeViewState: Equatable {
    case loading
    case loaded(nickname: String, email: String, point: Int, histories: [PointHistory])
    case error(String)
}

class HomeViewModel: ObservableObject {
    @Published var viewState: HomeViewState = .loading

    private let appSettingUseCase: AppSettingUseCaseProtocol
    private let pointUseCase: PointUseCaseProtocol
    private let historyUseCase: HistoryUseCaseProtocol
    private let loadAction: (() async -> Void)?

    init(
        appSettingUseCase: AppSettingUseCaseProtocol = KmpUseCaseFactory.shared.appSettingUseCase,
        pointUseCase: PointUseCaseProtocol = KmpUseCaseFactory.shared.pointUseCase,
        historyUseCase: HistoryUseCaseProtocol = KmpUseCaseFactory.shared.historyUseCase,
        loadAction: (() async -> Void)? = nil
    ) {
        self.appSettingUseCase = appSettingUseCase
        self.pointUseCase = pointUseCase
        self.historyUseCase = historyUseCase
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
            let appSetting = try await appSettingUseCase.find()
            let point = try await pointUseCase.find()
            let histories = try await historyUseCase.findAll()

            if let nickname = appSetting.nickName, let email = appSetting.email {
                self.viewState = .loaded(nickname: nickname, email: email, point: Int(point.balance), histories: histories)
            } else {
                self.viewState = .error("NickNameとEmailが不正です")
            }
        } catch {
            self.viewState = .error(error.localizedDescription)
        }
    }
}

// Mock
extension HomeViewModel {
    static func mock(_ state: HomeViewState) -> HomeViewModel {
        let vm = HomeViewModel(
            appSettingUseCase: DummyAppSettingUseCase(),
            pointUseCase: DummyPointUseCase(),
            historyUseCase: DummyHistoryUseCase(),
            loadAction: {}
        )
        vm.viewState = state
        return vm
    }
}
