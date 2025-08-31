import SwiftUI
import shared

struct PointGetConfirmView: View {
    let onBack: () -> Void
    let onClose: () -> Void
    
    @Environment(\.dismiss) private var dismiss
    // ViewModel のオーナーではないため @ObservedObject
    @ObservedObject private var viewModel: PointGetViewModel
    // ローカルの実行フラグのみ保持（結果はVMのEventを参照）
    @State private var startedAcquire: Bool = false
    
    init(
        onBack: @escaping () -> Void,
        onClose: @escaping () -> Void,
        viewModel: PointGetViewModel
    ) {
        self.onBack = onBack
        self.onClose = onClose
        self._viewModel = ObservedObject(initialValue: viewModel)
    }

    var body: some View {
        ZStack {
            Color.white.ignoresSafeArea()

            switch viewModel.viewState {
            case .loading:
                ProgressView()
            case .error(let message):
                Text(message)
                    .foregroundColor(.red)
                    .font(.system(size: 16))
            case .success(_, let inputPoint, _, _):
                PointGetConfirmContents(
                    inputPoint: inputPoint,
                    isAcquiring: startedAcquire && (viewModel.acquireEventState == nil),
                    acquireEventState: viewModel.acquireEventState,
                    onBack: onBack,
                    onExecute: {
                        guard !(startedAcquire && (viewModel.acquireEventState == nil)) else { return }
                        startedAcquire = true
                        Task { await viewModel.acquirePoint(inputPoint: inputPoint) }
                    },
                    onCompleteOk: {
                        startedAcquire = false
                        viewModel.acquireEventState = nil
                        // フロー完了時は親(UIKit)のナビゲーションスタックを閉じる
                        onClose()
                    },
                    onErrorOk: {
                        startedAcquire = false
                        viewModel.acquireEventState = nil
                    }
                )
            }
        }
    }

}

// MARK: - Contents (for Preview)
private struct PointGetConfirmContents: View {
    let inputPoint: Int
    let isAcquiring: Bool
    let acquireEventState: PointAcquireEventState?
    let onBack: () -> Void
    let onExecute: () -> Void
    let onCompleteOk: () -> Void
    let onErrorOk: () -> Void

    var body: some View {
        NavigationView {
            VStack(alignment: .center, spacing: 0) {
                Text("point_get_confirm_overview")
                    .foregroundColor(Color("themeColor"))
                    .fontWeight(.bold)

                Spacer().frame(height: 8)

                Text("point_get_confirm_detail")
                    .foregroundColor(.black)

                Spacer().frame(height: 24)

                Text("point_get_confirm_point_label")
                    .foregroundColor(Color("themeColor"))
                    .font(.title3)
                    .fontWeight(.semibold)

                Spacer().frame(height: 8)

                Text(String(inputPoint))
                    .foregroundColor(Color("themeColor"))
                    .font(.largeTitle)
                    .fontWeight(.bold)

                Spacer().frame(height: 32)

                Button(action: onExecute) {
                    if isAcquiring {
                        ProgressView()
                            .tint(.white)
                            .frame(maxWidth: .infinity)
                            .padding()
                            .background(Color("themeColor"))
                            .cornerRadius(8)
                    } else {
                        Text("point_get_confirm_execute_button")
                            .frame(maxWidth: .infinity)
                            .padding()
                            .background(Color("themeColor"))
                            .foregroundColor(.white)
                            .cornerRadius(8)
                    }
                }
                .disabled(isAcquiring)

                Spacer()
            }
            .padding(.horizontal, 24)
            .navigationTitle("point_get_title")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    Button(action: onBack) {
                        Image(systemName: "chevron.backward")
                            .foregroundColor(Color("white"))
                    }
                }
            }
            // 完了ダイアログ
            .alert(
                "point_get_confirm_complete_dialog_message",
                isPresented: Binding(
                    get: { acquireEventState == .success },
                    set: { presented in if !presented { onCompleteOk() } }
                )
            ) {
                Button("dialog_ok") { onCompleteOk() }
            }
            // エラーダイアログ
            .alert(
                "dialog_error_title",
                isPresented: Binding(
                    get: {
                        if case .error = acquireEventState { return true }
                        return false
                    },
                    set: { presented in if !presented { onErrorOk() } }
                )
            ) {
                Button("dialog_ok") { onErrorOk() }
            } message: {
                if case let .error(message) = acquireEventState {
                    Text(message)
                }
            }
        }
        .navigationViewStyle(StackNavigationViewStyle()) // iOS15
    }
}

// MARK: - Previews

struct PointGetConfirmView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            PointGetConfirmContents(
                inputPoint: 100,
                isAcquiring: false,
                acquireEventState: nil,
                onBack: {},
                onExecute: {},
                onCompleteOk: {},
                onErrorOk: {}
            ).previewDisplayName("確認 初期")

            PointGetConfirmContents(
                inputPoint: 100,
                isAcquiring: true,
                acquireEventState: nil,
                onBack: {},
                onExecute: {},
                onCompleteOk: {},
                onErrorOk: {}
            ).previewDisplayName("実行中")

            PointGetConfirmContents(
                inputPoint: 100,
                isAcquiring: false,
                acquireEventState: .error(message: "エラーが発生しました。"),
                onBack: {},
                onExecute: {},
                onCompleteOk: {},
                onErrorOk: {}
            ).previewDisplayName("エラー表示例")

            PointGetConfirmContents(
                inputPoint: 120,
                isAcquiring: false,
                acquireEventState: .success,
                onBack: {},
                onExecute: {},
                onCompleteOk: {},
                onErrorOk: {}
            ).previewDisplayName("完了ダイアログ")
        }
    }
}
