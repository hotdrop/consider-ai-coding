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
    @Published var errorMessage: AlertItem? = nil // String? から AlertItem? に変更

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
                // 引数ラベルを追加: nickname: uiState.inputNickName, email: uiState.inputEmail
                try await appSettingUseCase.registerUser(nickname: uiState.inputNickName, email: uiState.inputEmail)
                DispatchQueue.main.async {
                    self.uiState.isComplete = true
                    self.uiState.isLoading = false
                }
            } catch {
                DispatchQueue.main.async {
                    // AlertItemを生成して設定
                    self.errorMessage = AlertItem(message: error.localizedDescription)
                    self.uiState.isLoading = false
                }
            }
        }
    }
}
