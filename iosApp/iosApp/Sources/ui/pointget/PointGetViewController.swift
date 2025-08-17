import UIKit
import SwiftUI

final class PointGetViewController: UIViewController {
    // TODO ここでViewModelを作成する
    
    override func viewDidLoad() {
        super.viewDidLoad()
        let host = UIHostingController(rootView: PointGetView(onClose: {
            // TODO
        }))
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
