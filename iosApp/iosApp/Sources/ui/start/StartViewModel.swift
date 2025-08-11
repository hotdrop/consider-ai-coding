import Foundation
import Combine
import shared

class StartViewModel: ObservableObject {
    @Published var viewState: StartViewState = .idle
    @Published var errorAlertItem: StartErrorAlertItem?

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
            // no operation(not change UI)
            return
        } catch {
            viewState = .idle
            errorAlertItem = StartErrorAlertItem(message: error.localizedDescription)
        }
    }
}

struct StartErrorAlertItem: Identifiable {
    let id = UUID()
    let message: String
}

enum StartViewState: Equatable {
    case idle
    case loading
    case success
}
