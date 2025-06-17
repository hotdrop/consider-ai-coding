import SwiftUI
import shared // sharedモジュールをインポート
import Foundation // Foundationをインポート

@main
struct iOSApp: App {
    init() {
        // KmpUseCaseFactoryの初期化
        KmpUseCaseFactory.shared.doInit(pd: IosPlatformDependencies())
    }

	var body: some Scene {
		WindowGroup {
			MainView() // ContentView() から MainView() に変更
		}
	}
}
