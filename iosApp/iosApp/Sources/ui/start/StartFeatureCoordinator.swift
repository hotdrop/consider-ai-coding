import UIKit
import SwiftUI

final class StartFeatureCoordinator: FeatureCoordinator {
    private let nav: UINavigationController
    private let router: AppRouter

    init(nav: UINavigationController, router: AppRouter) {
        self.nav = nav
        self.router = router
    }
    
    func makeEntry() -> UIViewController {
        let vc = StartViewController(
            onBack: { [weak nav] in
                nav?.popViewController(animated: true)
            },
            onRegisterSuccess: { [router] in
                router.route(to: .home, style: .replaceRoot)
            }
        )
        return vc
    }
}
