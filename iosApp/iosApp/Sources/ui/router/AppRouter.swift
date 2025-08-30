///
/// AppRoute
/// ここに各機能のエントリポイントのRouteを追加する
///
enum AppRoute {
    case splash
    case start
    case home
    case pointget
}

// ルートスタイル。固定
enum RouteStyle { case replaceRoot, push, modal }

// 
protocol AppRouter {
    func route(to: AppRoute, style: RouteStyle)
}
