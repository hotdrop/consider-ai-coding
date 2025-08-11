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
                self.viewState = .loaded(userId)
            } else if user.isInitialized() {
                self.viewState = .error("User ID is missing despite being initialized.")
            } else {
                self.viewState = .firstTime
            }
        } catch is CancellationError {
            // 何もしない（UIを変えない）
            return
        }  catch {
            self.viewState = .error(error.localizedDescription)
        }
    }
}

enum MainViewState: Equatable {
    case loading
    case loaded(String) // userId
    case firstTime
    case error(String)
}
