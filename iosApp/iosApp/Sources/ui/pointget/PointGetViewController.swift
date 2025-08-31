import UIKit
import SwiftUI

final class PointGetViewController: UIViewController {
    private let onClose: () -> Void
    
    // UIViewController は SwiftUI の @StateObject を使わない
    private let viewModel: PointGetViewModel
    
    init(
        onClose: @escaping () -> Void,
        viewModel: PointGetViewModel = PointGetViewModel()
    ) {
        self.onClose = onClose
        self.viewModel = viewModel
        super.init(nibName:nil,bundle:nil)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // ViewModelをVCで生成し、Input/Confirmで共有する
        // 閉じる処理はUIKitのナビゲーションスタックをpopしてHomeへ戻す
        let root = PointGetInputView(
            onClose: onClose,
            viewModel: viewModel
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
