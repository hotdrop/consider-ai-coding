import SwiftUI
import shared

struct PointGetRouter {
    // MARK: - Input (root)
    static func input(
        viewModel: PointGetViewModel,
        onCloseFlow: @escaping () -> Void,
        onNavigateToConfirm: @escaping () -> Void
    ) -> some View {
        PointGetInputView(
            viewModel: viewModel,
            onNavigateToConfirm: onNavigateToConfirm
        )
        .navigationBarBackButtonHidden(false)
        .toolbar {
            // フローの先頭：戻る＝フローを閉じる（親に戻る）
            ToolbarItem(placement: .navigationBarLeading) {
                Button(action: { onCloseFlow() }) {
                    Image(systemName: "chevron.backward")
                        .foregroundColor(Color("white"))
                }
            }
        }
    }
    
    // MARK: - Confirm
    static func confirm(
        viewModel: PointGetViewModel,
        onCloseFlow: @escaping () -> Void
    ) -> some View {
        // iOS15系ではデフォルトのBackボタンでInputへ戻れるため隠さない
        PointGetConfirmView(viewModel: viewModel, onCloseFlow: onCloseFlow)
            .navigationBarBackButtonHidden(false)
    }
}
