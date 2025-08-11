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
        case .complete:
            complete(dismiss: dismiss)
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
            onNavigateToConfirm: {} // TODO
        ).navigationBarBackButtonHidden(false)
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    Button(action: { dismiss() }) {
                        Image(systemName: "arrow.backward")
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
        PointGetConfirmView()
            .navigationBarBackButtonHidden(true)
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    Button  {
                        path.wrappedValue.removeLast()
                    } label: {
                        Image(systemName: "arrow.backward")
                    }
                }
            }
    }
    
    // MARK: - Complete
    private static func complete(
        dismiss: DismissAction
    ) -> some View {
        PointGetCompleteView()
            .navigationBarBackButtonHidden(true)
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    Button {
                        dismiss()
                    } label: {
                        Image(systemName: "xmark")
                    }
                }
            }
    }
}
