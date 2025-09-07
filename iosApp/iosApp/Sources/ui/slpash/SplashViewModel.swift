import Foundation
import Combine
import shared

class SplashViewModel: ObservableObject {
    @Published var viewState: SplashViewState = .loading

    private let userUseCase: UserUseCase

    init(userUseCase: UserUseCase = KmpFactory.shared.useCaseFactory.userUseCase) {
        self.userUseCase = userUseCase
    }

    @MainActor
    func load() async {
        viewState = .loading
        do {
            let result = try await userUseCase.find()
            switch asEnumResult(result: result) {
            case .success(data: let data):
                if let user = data as? User {
                    if user.isInitialized(), let userId = user.userId {
                        self.viewState = .loaded(userId: userId)
                    } else {
                        self.viewState = .firstTime
                    }
                }
            case .error(error: let error):
                self.viewState = .error(message: error.message)
            }
        } catch is CancellationError {
            // no operation(not change UI)
            return
        }  catch {
            self.viewState = .error(message: error.localizedDescription)
        }
    }
}

enum SplashViewState: Equatable {
    case loading
    case loaded(userId: String)
    case firstTime
    case error(message: String)
}
