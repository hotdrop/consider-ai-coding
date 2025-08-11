import Foundation
import Combine
import shared

class MainViewModel: ObservableObject {
    @Published var viewState: MainViewState = .loading

    private let userUseCase: UserUseCase

    init(userUseCase: UserUseCase = KmpFactory.shared.useCaseFactory.userUseCase) {
        self.userUseCase = userUseCase
    }

    @MainActor
    func load() async {
        viewState = .loading
        do {
            let user = try await userUseCase.findForIos()
            if user.isInitialized(), let userId = user.userId {
                self.viewState = .loaded(userId: userId)
            } else {
                self.viewState = .firstTime
            }
        } catch is CancellationError {
            // no operation(not change UI)
            return
        }  catch {
            self.viewState = .error(message: error.localizedDescription)
        }
    }
}

enum MainViewState: Equatable {
    case loading
    case loaded(userId: String)
    case firstTime
    case error(message: String)
}
