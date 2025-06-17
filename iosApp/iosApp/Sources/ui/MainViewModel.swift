import Foundation
import Combine
import shared

enum MainViewState {
    case loading
    case loaded(String) // userId
    case firstTime
    case error(String)
}

class MainViewModel: ObservableObject {
    @Published var viewState: MainViewState = .loading

    private let appSettingUseCase: AppSettingUseCase = KmpUseCaseFactory().appSettingUseCase

    init() {
        loadAppSetting()
    }

    func loadAppSetting() {
        viewState = .loading
        Task {
            do {
                let appSetting = try await appSettingUseCase.find()
                DispatchQueue.main.async {
                    if appSetting.isInitialized() {
                        if let userId = appSetting.userId {
                            self.viewState = .loaded(userId)
                        } else {
                            // appSetting.isInitialized()がtrueなのにuserIdがnilの場合のハンドリング
                            self.viewState = .error("User ID is missing despite being initialized.")
                        }
                    } else {
                        self.viewState = .firstTime
                    }
                }
            } catch {
                DispatchQueue.main.async {
                    self.viewState = .error(error.localizedDescription)
                }
            }
        }
    }
}
