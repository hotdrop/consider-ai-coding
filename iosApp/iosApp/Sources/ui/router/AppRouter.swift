/// ルーターのプロトコル。Feature 側はこのインターフェースに依存し、
/// 具体実装（AppCoordinator への委譲）には依存しません（依存関係の逆転/DI のため）。
protocol AppRouter {
    /// 指定した `AppRoute` に、`style` の方法で遷移します。
    func route(to: AppRoute, style: RouteStyle)
}
