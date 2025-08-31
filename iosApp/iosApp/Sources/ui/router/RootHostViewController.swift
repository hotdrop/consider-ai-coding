import UIKit

/// SwiftUI の `CoordinatorHost` から表示される UIKit のルートコンテナ。
/// - アプリでただ一つの `UINavigationController` を子として保持し、
///   全ての画面遷移をここに集約します（NavigationStack は使用しない）。
/// - ステータスバー直下の背景色を安定して制御するため、Window 直下に
///   オーバーレイを敷く実装を持ちます。
final class RootHostViewController: UIViewController {
    private let nav = UINavigationController()
    private var appCoordinator: AppCoordinator!
    private var statusBarWindowOverlay: UIView?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // 子として UINavigationController を組み込み、レイアウトを全画面に制約します。
        addChild(nav)
        view.addSubview(nav.view)
        nav.view.translatesAutoresizingMaskIntoConstraints = false
        NSLayoutConstraint.activate([
            nav.view.topAnchor.constraint(equalTo: view.topAnchor),
            nav.view.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            nav.view.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            nav.view.bottomAnchor.constraint(equalTo: view.bottomAnchor),
        ])
        nav.didMove(toParent: self)

        // UIKit 側のナビゲーションバーは非表示にし、各々のView の NavigationView 側でバーを描画・制御する。
        nav.setNavigationBarHidden(true, animated: false)

        // 画面遷移の統括を AppCoordinator に委譲し、アプリを開始します。
        appCoordinator = AppCoordinator(navigationController: nav)
        appCoordinator.start()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        // Window 直下にステータスバー領域のオーバーレイを敷く（最も確実）
        installOrUpdateStatusBarOverlay()
    }

    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        // 回転やセーフエリア変化に追随
        installOrUpdateStatusBarOverlay()
    }
    
    /// ステータスバー背景用のオーバーレイビューを Window 直下に敷設/更新します。
    /// SwiftUI/UIView の階層や回転・SafeArea の変化に影響されない安定した背景色を実現します。
    private func installOrUpdateStatusBarOverlay() {
        guard let window = view.window else { return }

        // デフォルトの塗り高さ（ステータスバー相当）。取得できない場合はセーフエリア上端。
        let defaultHeight: CGFloat = {
            let h = window.windowScene?.statusBarManager?.statusBarFrame.height ?? 0
            return h > 0 ? h : window.safeAreaInsets.top
        }()

        // 画面階層に存在する（SwiftUI内部を含む）最上部の UINavigationBar の上端Y座標（Window基準）まで塗る
        let navTopY: CGFloat = findTopMostNavigationBarMinY(in: window) ?? defaultHeight

        let targetFrame = CGRect(x: 0, y: 0, width: window.bounds.width, height: navTopY)

        let overlay: UIView
        if let existing = statusBarWindowOverlay, existing.superview === window {
            overlay = existing
        } else {
            overlay = UIView(frame: targetFrame)
            overlay.isUserInteractionEnabled = false
            overlay.autoresizingMask = [.flexibleWidth, .flexibleBottomMargin]
            window.addSubview(overlay)
            statusBarWindowOverlay = overlay
        }
        overlay.frame = targetFrame
        overlay.backgroundColor = UIColor(named: "appbarColor")
        window.bringSubviewToFront(overlay)
    }

    /// Window 階層を走査して、可視な UINavigationBar 群のうち最も上にあるバーの上端Y（Window座標）を返します。
    private func findTopMostNavigationBarMinY(in window: UIWindow) -> CGFloat? {
        var minY: CGFloat?

        func traverse(_ view: UIView) {
            if let bar = view as? UINavigationBar,
               !bar.isHidden,
               bar.alpha > 0.01,
               bar.window === window {
                let rect = bar.convert(bar.bounds, to: window)
                let y = rect.minY
                if y.isFinite, y >= 0 {
                    if let current = minY {
                        minY = min(current, y)
                    } else {
                        minY = y
                    }
                }
            }
            for sub in view.subviews { traverse(sub) }
        }

        traverse(window)
        return minY
    }
}
