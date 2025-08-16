import SwiftUI
import shared

enum PointGetRoute: Hashable {
    case input
    case confirm
}

// PointGet サブフローの「独立スタック」ルート。
// ここが fullScreenCover 内で表示され、Main のスタックから完全に独立する。
struct PointGetView: View {
    @Environment(\.dismiss) private var dismiss
    @StateObject private var viewModel: PointGetViewModel
    
    @State private var path: [PointGetRoute] = []
    @State private var didTriggerInitialLoad = false
    @State private var didPushedInput = false
    
    let onClose: () -> Void

    init(viewModel: PointGetViewModel = PointGetViewModel(), onClose: @escaping () -> Void) {
        _viewModel = StateObject(wrappedValue: viewModel)
        self.onClose = onClose
    }

    var body: some View {
        NavigationStack(path: $path) {
            Group {
                switch viewModel.viewState {
                case .loading:
                    ProgressView().padding()
                case .success:
                    // フローはルートの path で管理（この画面はプレースホルダ）
                    Color.clear
                case .error(let message):
                    VStack(spacing: 12) {
                        Text(message).foregroundColor(.red)
                    }
                    .padding()
                }
            }
            .navigationTitle("point_get_title")
            .navigationBarTitleDisplayMode(.inline)
            .navigationDestination(for: PointGetRoute.self) { route in
                PointGetRouter.destination(
                    for: route,
                    viewModel: viewModel,
                    path: $path,
                    dismiss: dismiss
                )
            }
        }.task {
            guard !didTriggerInitialLoad else { return }
            didTriggerInitialLoad = true
            await viewModel.load()
        }
        .onChange(of: viewModel.viewState) { state in
            // success 到達時に input を一度だけ開始
            if case .success = state, !didPushedInput {
                didPushedInput = true
                path.append(.input)
            }
        }
    }
}
