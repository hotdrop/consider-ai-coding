import SwiftUI
import shared

struct PointGetConfirmView: View {
    @ObservedObject var viewModel: PointGetViewModel
    @State private var isAcquiring: Bool = false
    @State private var showError: Bool = false
    @State private var errorMessage: String = ""

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
                    isAcquiring: isAcquiring,
                    onExecute: {
                        guard !isAcquiring else { return }
                        isAcquiring = true
                        Task { await viewModel.acquirePoint(inputPoint: inputPoint) }
                    }
                )
            }
        }
        .onChange(of: viewModel.acquireEventState) { event in
            guard let event else { return }
            switch event {
            case .success:
                // 遷移は PointGetView 側の onChange で行う
                isAcquiring = false
            case .error(let message):
                isAcquiring = false
                errorMessage = message
                showError = true
            }
        }
        .alert("point_get_confirm_error", isPresented: $showError) {
            Button("dialog_ok") {
                showError = false
                viewModel.acquireEventState = nil
            }
        } message: {
            Text(errorMessage)
        }
    }

}

// MARK: - Contents (for Preview)

private struct PointGetConfirmContents: View {
    let inputPoint: Int
    let isAcquiring: Bool
    let onExecute: () -> Void

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
    }
}

// MARK: - Previews

struct PointGetConfirmView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            NavigationStack {
                PointGetConfirmContents(
                    inputPoint: 100,
                    isAcquiring: false,
                    onExecute: {}
                )
            }.previewDisplayName("確認 初期")

            NavigationStack {
                PointGetConfirmContents(
                    inputPoint: 100,
                    isAcquiring: true,
                    onExecute: {}
                )
            }.previewDisplayName("実行中")

            NavigationStack {
                VStack { Text("point_get_confirm_error").foregroundColor(.red) }
            }.previewDisplayName("エラー表示例")
        }
    }
}
