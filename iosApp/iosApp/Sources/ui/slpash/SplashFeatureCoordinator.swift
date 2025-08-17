import UIKit

final class SplashFeatureCoordinator: FeatureCoordinator {
    private let nav: UINavigationController
    private let router: AppRouter
    
    init(nav: UINavigationController, router: AppRouter) {
        self.nav = nav
        self.router = router
    }
    
    func makeEntry() -> UIViewController {
        let vc = SplashViewController(
            navigateToStart: { [router] in router.route(to: .start, style: .push) },
            navigateToHome: { [router] in router.route(to: .home, style: .replaceRoot) }
        )
        return vc
    }
}
