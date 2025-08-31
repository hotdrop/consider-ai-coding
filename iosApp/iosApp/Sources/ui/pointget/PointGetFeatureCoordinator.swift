import UIKit
import SwiftUI

final class PointGetFeatureCoordinator: FeatureCoordinator {
    private let nav: UINavigationController
    private let router: AppRouter
    
    init(nav: UINavigationController, router: AppRouter) {
        self.nav = nav
        self.router = router
    }
    
    func makeEntry() -> UIViewController {
        let vc = PointGetViewController(
            onBack: { [weak nav] in
                // TODO ホーム画面まで戻ってしまう、PointGetInputViewに戻ってほしい
                nav?.popViewController(animated: true)
            },
            onClose: { [weak nav] in
                nav?.popViewController(animated: true)
            }
        )
        return vc
    }
}
