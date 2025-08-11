import SwiftUI
import shared

enum PointGetRoute: Hashable {
    case input
    case confirm
    case complete
}

struct PointGetView: View {
    @Environment(\.dismiss) private var dismiss
    @StateObject private var viewModel: PointGetViewModel
    
    @State private var path: [PointGetRoute] = []
    @State private var didTriggerInitialLoad = false
    @State private var currentRoute: PointGetRoute?
    
    init(viewModel: PointGetViewModel = PointGetViewModel()) {
        _viewModel = StateObject(wrappedValue: viewModel)
    }
    
    var body: some View {
        NavigationStack(path: $path) {
            Group {
                switch viewModel.viewState {
                case .loading:
                    ProgressView().padding()
                case .success:
                    // フローはpathで管理するのでここは空
                    Color.clear
                case .error(let message):
                    Text(message).foregroundColor(.red).padding()
                }
            }
        }
        .navigationTitle("point_get_title")
        .navigationBarTitleDisplayMode(.inline)
        .navigationDestination(
            for: PointGetRoute.self,
            destination: { route in
                PointGetRouter.destination(
                    for: route,
                    viewModel: viewModel,
                    path: $path,
                    dismiss: dismiss
                )
            }
        )
        .task {
            // 初回ロード
            guard !didTriggerInitialLoad else { return }
            didTriggerInitialLoad = true
            await viewModel.load()
        }
        .onChange(of: viewModel.viewState) { state in
            // success 到達でフロー開始（input へ）
            if case .success = state, currentRoute == nil {
                path.append(.input)
            }
        }
        .onChange(of: viewModel.acquireEventState) { eventState in
            // 獲得イベントで complete へ（戻れない仕様）
            guard let eventState else { return }
            switch eventState {
            case .success:
                // 完了後は入力画面や確認画面には戻らない仕様のため、履歴を消して complete 画面のみ残す
                path.removeAll()
                path.append(.complete)
            case .error(let message):
                // ここでアラート表示などのエラー処理を行う（今回は Router 側でも各画面側でも処理可能）
                break
            default:
                break
            }
        }
        .onChange(of: path) { newPath in
            currentRoute = newPath.last
        }
    }
}
