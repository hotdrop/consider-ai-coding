import Foundation
import Combine
import shared

struct StartUiState {
    var inputNickName: String = ""
    var inputEmail: String = ""
    var isLoading: Bool = false
    var isComplete: Bool = false
}

class StartViewModel: ObservableObject {
    @Published var uiState: StartUiState = StartUiState()
    @Published var errorMessage: String? = nil

    private let appSettingUseCase: AppSettingUseCase = KmpUseCaseFactory().appSettingUseCase

    func onNickNameChanged(newValue: String) {
        uiState.inputNickName = newValue
    }

    func onEmailChanged(newValue: String) {
        uiState.inputEmail = newValue
    }

    func save() {
        uiState.isLoading = true
        errorMessage = nil // エラーメッセージをリセット

        Task {
            do {
                try await appSettingUseCase.registerUser(uiState.inputNickName, uiState.inputEmail)
                DispatchQueue.main.async {
                    self.uiState.isComplete = true
                    self.uiState.isLoading = false
                }
            } catch {
                DispatchQueue.main.async {
                    self.errorMessage = error.localizedDescription
                    self.uiState.isLoading = false
                }
            }
        }
    }
}
