import SwiftUI
import shared

///
/// このViewはPointGetViewを親に持ち、Navigationは全て親Viewが責務を持つ
///
struct PointGetConfirmView: View {
    @Environment(\.dismiss) private var dismiss
    @ObservedObject var viewModel: PointGetViewModel
    let onCloseFlow: () -> Void
    // ローカルの実行フラグのみ保持（結果はVMのEventを参照）
    @State private var startedAcquire: Bool = false

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
                    onExecute: {
                        guard !(startedAcquire && (viewModel.acquireEventState == nil)) else { return }
                        startedAcquire = true
                        Task { await viewModel.acquirePoint(inputPoint: inputPoint) }
                    },
                    onCompleteOk: {
                        startedAcquire = false
                        viewModel.acquireEventState = nil
                        // フロー完了時は親(UIKit)のナビゲーションスタックを閉じる
                        onCloseFlow()
                    },
                    onErrorOk: {
                        startedAcquire = false
                        viewModel.acquireEventState = nil
                    }
                )
            }
        }
        // アラートは Contents 側で表示
    }

}

// MARK: - Contents (for Preview)
private struct PointGetConfirmContents: View {
    let inputPoint: Int
    let isAcquiring: Bool
    let acquireEventState: PointAcquireEventState?
    let onExecute: () -> Void
    let onCompleteOk: () -> Void
    let onErrorOk: () -> Void

    var body: some View {
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
                inputPoint: 100,
                isAcquiring: false,
                acquireEventState: nil,
                onExecute: {},
                onCompleteOk: {},
                onErrorOk: {}
            ).previewDisplayName("確認 初期")

            PointGetConfirmContents(
                inputPoint: 100,
                isAcquiring: true,
                acquireEventState: nil,
                onExecute: {},
                onCompleteOk: {},
                onErrorOk: {}
            ).previewDisplayName("実行中")

            PointGetConfirmContents(
                inputPoint: 100,
                isAcquiring: false,
                acquireEventState: .error(message: "エラーが発生しました。"),
                onExecute: {},
                onCompleteOk: {},
                onErrorOk: {}
            ).previewDisplayName("エラー表示例")

            PointGetConfirmContents(
                inputPoint: 120,
                isAcquiring: false,
                acquireEventState: .success,
                onExecute: {},
                onCompleteOk: {},
                onErrorOk: {}
            ).previewDisplayName("完了ダイアログ")
        }
    }
}
