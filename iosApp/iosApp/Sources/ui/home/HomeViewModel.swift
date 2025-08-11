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
            async let userDefered = userUseCase.findForIos()
            async let pointDefered = pointUseCase.findForIos()
            async let historyDefered = historyUseCase.findAllForIos()

            let (userResult, pointResult, historiesResult) = try await (userDefered, pointDefered, historyDefered)
            viewState = .loaded(
                user: userResult,
                point: Int(pointResult.balance)
            )
            historyState = .loaded(histories: historiesResult)
        } catch is CancellationError {
            // no operation(not change UI)
            return
        } catch let e as AppError {
            switch (e) {
            case is AppError.NetworkError:
                viewState = .error(message: e.message)
                break
            case is AppError.ProgramError:
                viewState = .error(message: e.message)
                break
            case is AppError.UnknownError:
                viewState = .error(message: e.message)
                break
            default:
                viewState = .error(message: e.message)
                break
            }
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
