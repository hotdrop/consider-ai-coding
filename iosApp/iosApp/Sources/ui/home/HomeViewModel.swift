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
        viewState = .initialLoading
        
        do {
            async let userDefered = userUseCase.find()
            async let pointDefered = pointUseCase.find()
            async let historyDefered = historyUseCase.findAll()

            let (userResult, pointResult, historiesResult) = try await (userDefered, pointDefered, historyDefered)
            
            var user: User?
            
            switch asEnumResult(result: userResult) {
            case .success(let userResult):
                user = userResult
            case .error(let error):
                viewState = .error(message: error.message)
            }
            
            switch asEnumResult(result: pointResult) {
            case .success(let pointResult):
                viewState = .loaded(
                    user: user!,
                    point: Int(pointResult.balance)
                )
            case .error(let error):
                viewState = .error(message: error.message)
            }
            
            switch asEnumResult(result: historiesResult) {
            case .success(let data):
                // Kotlin/Native の List<T> は Swift 側では [T] ではない場合があります。
                // そのため、ここは既存動作を保ちつつ安全にキャストを試みます。
                if let historiesResult = data as? [PointHistory] {
                    historyState = .loaded(histories: historiesResult)
                }
            case .error(let error):
                viewState = .error(message: error.message)
            }
        } catch is CancellationError {
            // no operation(not change UI)
            return
        } catch {
            viewState = .error(message: "Unknown Error")
        }
    }
}

enum HomeViewState: Equatable {
    case initialLoading
    case loaded(user: User, point: Int)
    case error(message: String)
}

enum HistoryState: Equatable {
    case loading
    case loaded(histories: [PointHistory])
    case error(message: String)
}
