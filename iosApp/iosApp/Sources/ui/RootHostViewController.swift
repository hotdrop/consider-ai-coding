import UIKit

final class RootHostViewController: UIViewController {
    private let nav = UINavigationController()
    private var appCoordinator: AppCoordinator!
    
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
        
        appCoordinator = AppCoordinator(navigationController: nav)
        appCoordinator.start()
    }
}
