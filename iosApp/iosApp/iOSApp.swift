import SwiftUI
import Foundation
import shared

@main
struct iOSApp: App {
    init() {
        KmpFactory.shared.doInit(pd: IosPlatformDependencies())
        
        let appearance = UINavigationBarAppearance()
        appearance.configureWithOpaqueBackground()
        appearance.backgroundColor = UIColor(named: "appbarColor")
        appearance.titleTextAttributes = [
            .foregroundColor: UIColor.white,
            .font: UIFont.systemFont(ofSize: 24, weight: .medium)
        ]
        
        UINavigationBar.appearance().standardAppearance = appearance
        UINavigationBar.appearance().scrollEdgeAppearance = appearance
    }

	var body: some Scene {
		WindowGroup {
            CoordinatorHost()
		}
	}
}
