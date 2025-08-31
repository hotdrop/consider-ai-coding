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

/// ルーターのプロトコル。Feature 側はこのインターフェースに依存し、
/// 具体実装（AppCoordinator への委譲）には依存しません（依存関係の逆転/DI のため）。
protocol AppRouter {
    /// 指定した `AppRoute` に、`style` の方法で遷移します。
    func route(to: AppRoute, style: RouteStyle)
}
