import Foundation
import Combine
import shared

enum MainViewState {
    case loading
    case loaded(String) // userId
    case firstTime
    case error(String)
}

@MainActor // MainActorを追加
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
                // @MainActorクラスなので、DispatchQueue.main.asyncは不要
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
            } catch {
                // @MainActorクラスなので、DispatchQueue.main.asyncは不要
                self.viewState = .error(error.localizedDescription)
            }
        }
    }
}
