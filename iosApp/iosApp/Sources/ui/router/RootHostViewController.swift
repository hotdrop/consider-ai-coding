import UIKit

final class RootHostViewController: UIViewController {
    private let nav = UINavigationController()
    private var appCoordinator: AppCoordinator!
    private var statusBarWindowOverlay: UIView?
    
    override func viewDidLoad() {
        super.viewDidLoad()

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

        // UIKit 側のナビゲーションバーは非表示にし、各々のView の NavigationView/NavigationStack 側でバーを描画・制御する。
        nav.setNavigationBarHidden(true, animated: false)

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
    
    private func installOrUpdateStatusBarOverlay() {
        guard let window = view.window,
              let statusFrame = window.windowScene?.statusBarManager?.statusBarFrame else { return }

        let overlay: UIView
        if let existing = statusBarWindowOverlay, existing.superview === window {
            overlay = existing
        } else {
            overlay = UIView(frame: statusFrame)
            overlay.isUserInteractionEnabled = false
            overlay.autoresizingMask = [.flexibleWidth, .flexibleBottomMargin]
            window.addSubview(overlay)
            statusBarWindowOverlay = overlay
        }
        overlay.frame = statusFrame
        overlay.backgroundColor = UIColor(named: "appbarColor")
        window.bringSubviewToFront(overlay)
    }
}
