import UIKit
import SwiftUI

final class PointGetViewController: UIViewController {
    private let viewModel = PointGetViewModel()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // ViewModelをVCで生成し、Input/Confirmで共有する
        // 閉じる処理はUIKitのナビゲーションスタックをpopしてHomeへ戻す
        let root = PointGetView(
            viewModel: viewModel,
            onCloseFlow: { [weak self] in
                self?.navigationController?.popViewController(animated: true)
            }
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
