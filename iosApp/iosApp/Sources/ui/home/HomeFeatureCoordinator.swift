import UIKit
import SwiftUI

final class HomeFeatureCoordinator: FeatureCoordinator {
    private let nav: UINavigationController
    private let router: AppRouter
    
    init(nav: UINavigationController, router: AppRouter) {
        self.nav = nav
        self.router = router
    }
    
    func makeEntry() -> UIViewController {
        let vc = HomeViewController(onTapPointGet: { [router] in
            router.route(to: .pointget, style: .push)
        })
        return vc
    }
}
