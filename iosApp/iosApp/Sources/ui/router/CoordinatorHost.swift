import SwiftUI

struct CoordinatorHost: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        RootHostViewController()
    }
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
