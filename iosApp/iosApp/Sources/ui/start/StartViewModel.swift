import Foundation
import Combine
import shared

class StartViewModel: ObservableObject {
    @Published var viewState: StartViewState = .idle
    @Published var errorAlertItem: ErrorAlertItem?

    private let userUseCase: UserUseCase

    init(
        userUseCase: UserUseCase = KmpFactory.shared.useCaseFactory.userUseCase
    ) {
        self.userUseCase = userUseCase
    }

    @MainActor
    func registerUser(nickname: String, email: String) async {
        viewState = .loading
        
        do {
            try await userUseCase.registerUserForIos(nickname: nickname, email: email)
            viewState = .success
        } catch is CancellationError {
            // TODO UIを変えない/戻すなどの処理を行う
        } catch {
            viewState = .idle
            errorAlertItem = ErrorAlertItem(message: error.localizedDescription)
        }
    }
}

struct ErrorAlertItem: Identifiable, Equatable {
    let id = UUID()
    let message: String
}

enum StartViewState: Equatable {
    case idle
    case loading
    case success
}
