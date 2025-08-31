import SwiftUI

/// SwiftUI 側のエントリから UIKit のルートコンテナ（`RootHostViewController`）を
/// ブリッジするためのホスト。NavigationStack は使わず、UIKit の
/// `UINavigationController` に一本化したナビゲーションを提供します。
struct CoordinatorHost: UIViewControllerRepresentable {
    /// SwiftUI ツリー直下に表示する UIKit ルート VC を生成します。
    func makeUIViewController(context: Context) -> UIViewController {
        RootHostViewController()
    }
    /// SwiftUI の再描画に伴う更新。ここでは特に更新処理は不要です。
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
