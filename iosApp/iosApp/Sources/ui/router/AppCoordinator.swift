import UIKit

/// アプリ全体で利用する「行き先（画面/機能）」を表す列挙型。
/// AppCoordinator がこの値に応じて、各機能のエントリポイント（FeatureCoordinator）を生成し、
/// 遷移（push/置換/モーダル）を実行します。
///
/// 新しい機能を追加する場合は、以下の2点を実施します:
/// 1. この `AppRoute` にケースを追加する
/// 2. `AppCoordinator.makeCoordinator(for:)` にルーティング先の FeatureCoordinator を関連付ける
enum AppRoute {
    /// アプリ起動直後のスプラッシュ。認証状態の確認などの起点。
    case splash
    /// 初期登録/ログインなど、利用開始フローの起点。
    case start
    /// ログイン後のホーム画面（アプリのメイン）。
    case home
    /// ポイント取得フロー（ホーム等から遷移）。
    case pointget
}

/// 画面遷移のスタイル（実行方法）。
/// AppCoordinator が `UINavigationController` を用いて実装します。
/// - replaceRoot: 現在のスタックを入れ替え、戻る操作ができないルート差し替え
/// - push: ナビゲーションスタックに積む（戻る可能）
/// - modal: 現在のトップからモーダル表示
enum RouteStyle { case replaceRoot, push, modal }

/// アプリ全体の画面遷移を統括するコーディネータ。
/// - 単一の `UINavigationController` を使って遷移を一元管理します。
/// - `AppRoute`（行き先）から該当の `FeatureCoordinator` を生成し、
///   そのエントリとなる `UIViewController` を取得して、push/置換/モーダルを実行します。
/// - SwiftUI の NavigationStack は採用せず、UIKit のナビゲーションに統合しています。
final class AppCoordinator {
    /// アプリでただ一つのナビゲーションスタック。
    private let nav: UINavigationController
    /// Feature 側から受けるルーティング依頼の窓口（DI）。
    private lazy var router: AppRouter = DefaultAppRouter(app: self)
    
    init(navigationController: UINavigationController) {
        self.nav = navigationController
    }
    
    /// `AppRoute` と各機能の `FeatureCoordinator` を対応付けるファクトリ。
    /// 新しい画面フローを追加する場合は、ここに `switch` の分岐を追加します。
    private func makeCoordinator(for route: AppRoute) -> FeatureCoordinator {
        switch route {
        case .splash: return SplashFeatureCoordinator(nav: nav, router: router)
        case .start: return StartFeatureCoordinator(nav: nav, router: router)
        case .home: return HomeFeatureCoordinator(nav: nav, router: router)
        case .pointget: return PointGetFeatureCoordinator(nav: nav, router: router)
        }
    }
    
    /// アプリのエントリポイント。起動時に呼び出して最初の画面を表示します。
    /// 既存スタックは存在しないため `replaceRoot` でルート差し替えします。
    func start() {
        dispatch(route: .splash, style: .replaceRoot)
    }
    
    /// 遷移の実体。指定の `FeatureCoordinator` からエントリ用 VC を受け取り、
    /// `style` に応じて `UINavigationController` 上で画面遷移を実行します。
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


/// `AppRouter` の標準実装。外部（Feature 側）からのルーティング要求を
/// `AppCoordinator` の `dispatch` に委譲して実行します。
final class DefaultAppRouter: AppRouter {
    /// 循環参照を避けるため `unowned` 参照で保持します（ライフサイクルは `RootHostViewController` が管理）。
    private unowned let app: AppCoordinator
    init(app: AppCoordinator) {
        self.app = app
    }
    /// 指定の行き先とスタイルで画面遷移を行います。
    func route(to: AppRoute, style: RouteStyle) {
        app.dispatch(route: to, style: style)
    }
}
