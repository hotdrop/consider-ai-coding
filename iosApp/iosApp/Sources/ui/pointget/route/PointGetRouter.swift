import SwiftUI
import shared

struct PointGetRouter {
    @ViewBuilder
    static func destination(
        for route: PointGetRoute,
        viewModel: PointGetViewModel,
        path: Binding<[PointGetRoute]>,
        dismiss: DismissAction
    ) -> some View {
        switch route {
        case .input:
            input(viewModel: viewModel, path: path, dismiss: dismiss)
        case .confirm:
            confirm(viewModel: viewModel, path: path)
        }
    }
    
    // MARK: - Input
    private static func input(
        viewModel: PointGetViewModel,
        path: Binding<[PointGetRoute]>,
        dismiss: DismissAction
    ) -> some View {
        PointGetInputView(
            viewModel: viewModel,
            onNavigateToConfirm: {
                path.wrappedValue.append(.confirm)
            }
        )
        .navigationBarBackButtonHidden(false)
        .toolbar {
            // フローの先頭：戻る＝フローを閉じる（親に戻る）
            ToolbarItem(placement: .navigationBarLeading) {
                Button(action: { dismiss() }) {
                    Image(systemName: "chevron.backward")
                }
            }
        }
    }
    
    // MARK: - Confirm
    private static func confirm(
        viewModel: PointGetViewModel,
        path: Binding<[PointGetRoute]>
    ) -> some View {
        // navigationBarBackButtonHidden(true) を設定し、戻る操作は自前で用意したボタンからのみ行う
        PointGetConfirmView(viewModel: viewModel)
            .navigationBarBackButtonHidden(true)
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    Button  {
                        path.wrappedValue.removeLast()
                    } label: {
                        Image(systemName: "chevron.backward")
                    }
                }
            }
    }
}
