import UIKit
import SwiftUI

final class StartViewController: UIViewController {
    private let onBack: () -> Void
    private let onRegisterSuccess: () -> Void
    
    init(
        onBack: @escaping () -> Void,
        onRegisterSuccess: @escaping () -> Void
    ) {
        self.onBack = onBack
        self.onRegisterSuccess = onRegisterSuccess
        super.init(nibName:nil,bundle:nil)
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        let root = StartView(
            onBack: onBack,
            onRegisterSuccess: onRegisterSuccess
        )
        let host = UIHostingController(rootView: root)

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
