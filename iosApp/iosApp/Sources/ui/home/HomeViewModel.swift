import Foundation
import Combine
import shared

class HomeViewModel: ObservableObject {
    @Published var viewState: HomeViewState = .initialLoading
    @Published var historyState: HistoryState = .loading

    private let appSettingUseCase: AppSettingUseCaseProtocol
    private let pointUseCase: PointUseCaseProtocol
    private let historyUseCase: HistoryUseCaseProtocol
    private let loadAction: (() async -> Void)?

    init(
        appSettingUseCase: AppSettingUseCaseProtocol = KmpFactory.shared.useCaseFactory.appSettingUseCase,
        pointUseCase: PointUseCaseProtocol = KmpFactory.shared.useCaseFactory.pointUseCase,
        historyUseCase: HistoryUseCaseProtocol = KmpFactory.shared.useCaseFactory.historyUseCase,
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
            self.viewState = .initialLoading
            async let appSetting = appSettingUseCase.find()
            async let point = pointUseCase.find()

            let (appSettingResult, pointResult) = try await (appSetting, point)

            if let nickname = appSettingResult.nickName, let email = appSettingResult.email {
                self.viewState = .loaded(nickname: nickname, email: email, point: Int(pointResult.balance))
            } else {
                self.viewState = .error("NickNameとEmailが不正です")
            }
        } catch {
            self.viewState = .error(error.localizedDescription)
            return
        }

        do {
            self.historyState = .loading
            let histories = try await historyUseCase.findAll()
            self.historyState = .loaded(histories)
        } catch {
            self.historyState = .error(error.localizedDescription)
        }
    }
}

// Mock
extension HomeViewModel {
    static func mock(viewState: HomeViewState, historyState: HistoryState) -> HomeViewModel {
        let vm = HomeViewModel(
            appSettingUseCase: DummyAppSettingUseCase(),
            pointUseCase: DummyPointUseCase(),
            historyUseCase: DummyHistoryUseCase(),
            loadAction: {}
        )
        vm.viewState = viewState
        vm.historyState = historyState
        return vm
    }
}

enum HomeViewState: Equatable {
    case initialLoading
    case loaded(nickname: String, email: String, point: Int)
    case error(String)
}

enum HistoryState: Equatable {
    case loading
    case loaded([PointHistory])
    case error(String)
}
