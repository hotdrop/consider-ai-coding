import UIKit

/// 各「機能（Feature）」のフローを開始するためのエントリポイントを表すプロトコル。
/// - 実装クラスは、必要な依存（`UINavigationController` と `AppRouter`）を受け取り、
///   最初に表示すべき `UIViewController` を組み立てて返します。
/// - SwiftUI 画面を使う場合は、`UIHostingController` でラップした VC を返すなど、
///   UIKit の世界に橋渡しして返す想定です。
/// - 画面遷移（次の画面へ進む/戻る）は、`AppRouter` を通して AppCoordinator に委譲します。
protocol FeatureCoordinator {
    /// 機能のフローを開始する最初の `UIViewController` を構築して返します。
    func makeEntry() -> UIViewController
}
