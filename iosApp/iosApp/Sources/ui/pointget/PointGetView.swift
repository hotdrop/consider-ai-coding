import SwiftUI
import shared

enum PointGetRoute: Hashable {
    case input
    case confirm
}

// ルート NavigationStack を利用するため、ここでは NavigationStack を持たない
struct PointGetView: View {
    @Environment(\.dismiss) private var dismiss
    @StateObject var viewModel: PointGetViewModel
    @Binding var rootPath: NavigationPath

    @State private var didTriggerInitialLoad = false
    @State private var didPushedInput = false

    init(viewModel: PointGetViewModel = PointGetViewModel(), rootPath: Binding<NavigationPath>) {
        _viewModel = StateObject(wrappedValue: viewModel)
        _rootPath = rootPath
    }

    var body: some View {
        Group {
            switch viewModel.viewState {
            case .loading:
                ProgressView().padding()
            case .success:
                // フローはルートの path で管理（この画面はプレースホルダ）
                Color.clear
            case .error(let message):
                Text(message).foregroundColor(.red).padding()
            }
        }
        .navigationTitle("point_get_title")
        .navigationBarTitleDisplayMode(.inline)
        .task {
            guard !didTriggerInitialLoad else { return }
            didTriggerInitialLoad = true
            await viewModel.load()
        }
        .onChange(of: viewModel.viewState) { state in
            if case .success = state, !didPushedInput {
                didPushedInput = true
                rootPath.append(PointGetRoute.input)
            }
        }
    }
}
