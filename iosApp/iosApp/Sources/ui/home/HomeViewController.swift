import UIKit
import SwiftUI

final class HomeViewController: UIViewController {
    private let onTapPointGet: () -> Void
    
    init(onTapPointGet: @escaping () -> Void) {
        self.onTapPointGet = onTapPointGet
        super.init(nibName:nil,bundle:nil)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let root = HomeView(
            onNavigateToPointGet: onTapPointGet
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
