import UIKit

final class AppCoordinator {
    private let nav: UINavigationController
    private lazy var router: AppRouter = DefaultAppRouter(app: self)
    
    init(navigationController: UINavigationController) {
        self.nav = navigationController
    }
    
    // 機能のエントリポイント(FeatureCoodinator)をここに追加していく
    private func makeCoordinator(for route: AppRoute) -> FeatureCoordinator {
        switch route {
        case .splash: return SplashFeatureCoordinator(nav: nav, router: router)
        case .start: return StartFeatureCoordinator(nav: nav, router: router)
        case .home: return HomeFeatureCoordinator(nav: nav, router: router)
        case .pointget: return PointGetFeatureCoordinator(nav: nav, router: router)
        }
    }
    
    // アプリのエントリポイント
    func start() {
        dispatch(route: .splash, style: .replaceRoot)
    }
    
    func dispatch(route: AppRoute, style: RouteStyle) {
        let feature = makeCoordinator(for: route)
        let vc = feature.makeEntry()
        switch style {
        case .replaceRoot:
            nav.setViewControllers([vc], animated: false)
        case .push:
            nav.pushViewController(vc, animated: true)
        case .modal:
            nav.topViewController?.present(vc, animated: true)
        }
    }
}


final class DefaultAppRouter: AppRouter {
    private unowned let app: AppCoordinator
    init(app: AppCoordinator) {
        self.app = app
    }
    func route(to: AppRoute, style: RouteStyle) {
        app.dispatch(route: to, style: style)
    }
}

protocol FeatureCoordinator {
    func makeEntry() -> UIViewController
}
