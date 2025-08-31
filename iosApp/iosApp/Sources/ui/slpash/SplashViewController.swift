import UIKit
import SwiftUI

final class SplashViewController: UIViewController {
    private let navigateToStart: () -> Void
    private let navigateToHome: () -> Void
    
    init(navigateToStart: @escaping () -> Void, navigateToHome:  @escaping () -> Void) {
        self.navigateToStart = navigateToStart
        self.navigateToHome = navigateToHome
        super.init(nibName: nil, bundle: nil)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        let root = SplashView(
            navigateToStart: navigateToStart,
            navigateToHome: navigateToHome
        )
        let host = UIHostingController(rootView: root)
        // SwiftUI 側の背景はクリアにし、親（RootHost）の appbarColor が透けるようにする
        host.view.backgroundColor = .clear
        
        addChild(host)
        view.addSubview(host.view)
        host.view.translatesAutoresizingMaskIntoConstraints = false
        NSLayoutConstraint.activate([
            host.view.topAnchor.constraint(equalTo: view.topAnchor),
            host.view.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            host.view.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            host.view.bottomAnchor.constraint(equalTo: view.bottomAnchor),
        ])
        host.didMove(toParent: self)
    }
}
