import SwiftUI
import shared

struct PointGetConfirmView: View {
    let onClose: () -> Void
    
    @Environment(\.dismiss) private var dismiss
    // ViewModel のオーナーではないため @ObservedObject
    @ObservedObject private var viewModel: PointGetViewModel
    // ローカルの実行フラグのみ保持（結果はVMのEventを参照）
    @State private var startedAcquire: Bool = false
    
    init(
        onClose: @escaping () -> Void,
        viewModel: PointGetViewModel
    ) {
        self.onClose = onClose
        self._viewModel = ObservedObject(initialValue: viewModel)
    }

    var body: some View {
        PointGetConfirmContents(
            viewState: viewModel.viewState,
            isAcquiring: startedAcquire && (viewModel.acquireEventState == nil),
            acquireEventState: viewModel.acquireEventState,
            onBack: {
                dismiss()
            },
            onExecute: { inputPoint in
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

// MARK: - Contents
private struct PointGetConfirmContents: View {
    let viewState: PointGetViewState
    let isAcquiring: Bool
    let acquireEventState: PointAcquireEventState?
    let onBack: () -> Void
    let onExecute: (Int) -> Void
    let onCompleteOk: () -> Void
    let onErrorOk: () -> Void

    var body: some View {
        NavigationView {
            ZStack {
                switch viewState {
                case .loading:
                    ProgressView()
                        .progressViewStyle(CircularProgressViewStyle(tint: Color("themeColor")))
                
                case .success(_, let inputPoint, _, _):
                    LoadedView(
                        inputPoint: inputPoint,
                        isAcquiring: isAcquiring,
                        acquireEventState: acquireEventState,
                        onBack: onBack,
                        onExecute: onExecute,
                        onCompleteOk: onCompleteOk,
                        onErrorOk: onErrorOk
                    )
                case .error(let message):
                    Text(message)
                        .foregroundColor(.red)
                        .font(.system(size: 16))
                }
            }
            .padding(.horizontal, 24)
            .navigationTitle("point_get_title")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                // 確認画面の戻るは SwiftUI 内で1画面戻す
                ToolbarItem(placement: .navigationBarLeading) {
                    Button(action: { onBack() }) {
                        Image(systemName: "chevron.backward")
                            .foregroundColor(Color("white"))
                    }
                }
            }
        }
        
    }
}

// MARK: - LoadedView
private struct LoadedView: View {
    let inputPoint: Int
    let isAcquiring: Bool
    let acquireEventState: PointAcquireEventState?
    let onBack: () -> Void
    let onExecute: (Int) -> Void
    let onCompleteOk: () -> Void
    let onErrorOk: () -> Void

    var body: some View {
        VStack(spacing: 16) {
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

            Button(action: {
                onExecute(inputPoint)
            }) {
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
}

// MARK: - Previews

struct PointGetConfirmView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            PointGetConfirmContents(
                viewState: .success(
                    currentPoint: Point(balance: 1000),
                    inputPoint: 100,
                    errorMessage: nil,
                    isEnableConfirm: true
                ),
                isAcquiring: false,
                acquireEventState: nil,
                onBack: {},
                onExecute: { inputPoint in },
                onCompleteOk: {},
                onErrorOk: {}
            ).previewDisplayName("初期表示")
           
            PointGetConfirmContents(
                viewState: .success(
                    currentPoint: Point(balance: 1000),
                    inputPoint: 100,
                    errorMessage: nil,
                    isEnableConfirm: true
                ),
                isAcquiring: true,
                acquireEventState: nil,
                onBack: {},
                onExecute: { inputPoint in },
                onCompleteOk: {},
                onErrorOk: {}
            ).previewDisplayName("実行中")

            PointGetConfirmContents(
                viewState: .success(
                    currentPoint: Point(balance: 1000),
                    inputPoint: 100,
                    errorMessage: nil,
                    isEnableConfirm: true
                ),
                isAcquiring: false,
                acquireEventState: .error(message: "エラーが発生しました。"),
                onBack: {},
                onExecute: { inputPoint in },
                onCompleteOk: {},
                onErrorOk: {}
            ).previewDisplayName("エラー表示例")

            PointGetConfirmContents(
                viewState: .success(
                    currentPoint: Point(balance: 1000),
                    inputPoint: 120,
                    errorMessage: nil,
                    isEnableConfirm: true
                ),
                isAcquiring: false,
                acquireEventState: .success,
                onBack: {},
                onExecute: { inputPoint in },
                onCompleteOk: {},
                onErrorOk: {}
            ).previewDisplayName("完了ダイアログ")
        }
    }
}
