import Foundation
import Combine
import shared

class HomeViewModel: ObservableObject {
    @Published var viewState: HomeViewState = .initialLoading
    @Published var historyState: HistoryState = .loading

    private let userUseCase: UserUseCase
    private let pointUseCase: PointUseCase
    private let historyUseCase: HistoryUseCase

    init(
        userUseCase: UserUseCase = KmpFactory.shared.useCaseFactory.userUseCase,
        pointUseCase: PointUseCase = KmpFactory.shared.useCaseFactory.pointUseCase,
        historyUseCase: HistoryUseCase = KmpFactory.shared.useCaseFactory.historyUseCase
    ) {
        self.userUseCase = userUseCase
        self.pointUseCase = pointUseCase
        self.historyUseCase = historyUseCase
    }

    @MainActor
    func load() async {
        if Task.isCancelled { return }
        
        do {
            self.viewState = .initialLoading
            async let user = userUseCase.find()
            async let point = pointUseCase.find()

            let (userResult, pointResult) = try await (user, point)
            self.viewState = .loaded(user: user, point: Int(pointResult))
        } catch {
            self.viewState = .error(error.localizedDescription)
            return
        }

        do {
            self.historyState = .loading
            let result = try await historyUseCase.findAll() as! Result<[PointHistory], any Error>
            switch result {
            case .success(let histories):
                self.historyState = .loaded(histories)
            case .failure(let err):
                fatalError("\(err)")
            }
        } catch {
            self.historyState = .error(error.localizedDescription)
        }
    }
}

enum HomeViewState: Equatable {
    case initialLoading
    case loaded(user: User, point: Int)
    case error(String)
}

enum HistoryState: Equatable {
    case loading
    case loaded([PointHistory])
    case error(String)
}
