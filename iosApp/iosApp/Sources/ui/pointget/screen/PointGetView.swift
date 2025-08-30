import SwiftUI
import shared

// 入力画面をルートに据えた、ポイント獲得フロー用のNavigationView（iOS14/15対応）。
// ViewModelはVCで生成して注入され、このスタック内で共有される。
struct PointGetView: View {
    @StateObject private var viewModel: PointGetViewModel
    private let onCloseFlow: () -> Void

    @State private var isActiveConfirm: Bool = false

    init(viewModel: PointGetViewModel, onCloseFlow: @escaping () -> Void) {
        _viewModel = StateObject(wrappedValue: viewModel)
        self.onCloseFlow = onCloseFlow
    }

    var body: some View {
        NavigationView {
            ZStack {
                // 非表示のNavigationLinkでConfirmへ遷移
                NavigationLink(
                    destination: PointGetRouter.confirm(viewModel: viewModel, onCloseFlow: onCloseFlow),
                    isActive: $isActiveConfirm
                ) { EmptyView() }
                .hidden()

                // ルートはInput画面
                PointGetRouter.input(
                    viewModel: viewModel,
                    onCloseFlow: onCloseFlow,
                    onNavigateToConfirm: { isActiveConfirm = true }
                )
            }
            .navigationTitle("point_get_title")
            .navigationBarTitleDisplayMode(.inline)
        }
        .navigationViewStyle(StackNavigationViewStyle())
    }
}
