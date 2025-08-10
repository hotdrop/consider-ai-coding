
import SwiftUI
import shared

struct PointGetInputView: View {

    @StateObject private var viewModel: PointGetViewModel
    private var onNavigateToConfirm: (Int) -> Void
    private var onBack: () -> Void

    init(
        viewModel: PointGetViewModel = PointGetViewModel(),
        onNavigateToConfirm: @escaping (Int) -> Void,
        onBack: @escaping () -> Void
    ) {
        _viewModel = StateObject(wrappedValue: viewModel)
        self.onNavigateToConfirm = onNavigateToConfirm
        self.onBack = onBack
    }

    var body: some View {
        NavigationStack {
            ZStack {
                PointGetInputContents(
                    viewState: viewModel.viewState,
                    onInputPoint: { newValue in
                        viewModel.inputPoint(newInputPoint: newValue)
                    },
                    onNavigateToConfirm: { newValue in
                        onNavigateToConfirm(newValue)
                    }
                )
            }
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .principal) {
                    Text("ポイント獲得")
                }
                ToolbarItem(placement: .navigationBarLeading) {
                    Button(action: onBack) {
                        Image(systemName: "arrow.backward")
                    }
                }
            }
        }
        .task {
            await viewModel.load()
        }
    }
}

// MARK: - PointGetInputContents
private struct PointGetInputContents: View {
    let viewState: PointGetViewState
    let onInputPoint: (Int) -> Void
    let onNavigateToConfirm: (Int) -> Void
    
    var body: some View {
        Color.white.ignoresSafeArea()
        switch viewState {
        case .loading:
            ProgressView()
        case .success(let currentPoint, let inputPoint, let errorMessage, let isEnableConfirm):
            PointGetInputContentView(
                currentPoint: currentPoint,
                inputPoint: inputPoint,
                errorMessage: errorMessage,
                isEnableConfirm: isEnableConfirm,
                onInputChanged: { newValue in
                    onInputPoint(newValue)
                },
                onNavigateToConfirm: {
                    onNavigateToConfirm(inputPoint)
                }
            )
        case .error(let message):
            Text(message)
                .foregroundColor(.red)
                .font(.system(size: 16))
        }
    }
}

@ViewBuilder
private func PointGetInputContentView(
    currentPoint: Point,
    inputPoint: Int,
    errorMessage: String?,
    isEnableConfirm: Bool,
    onInputChanged: @escaping (Int) -> Void,
    onNavigateToConfirm: @escaping () -> Void
) -> some View {
    VStack(alignment: .center, spacing: 0) {
        PointGetOverview(
            balance: Int(currentPoint.balance),
            maxAvailable: 20000 - Int(currentPoint.balance) // TODO
        )
        .padding(.top, 16)

        Spacer().frame(height: 24)

        PointGetInputField(
            inputPoint: inputPoint,
            errorMessage: errorMessage,
            onValueChange: onInputChanged
        )

        Spacer().frame(height: 32)

        PointGetConfirmButton(
            isEnabled: isEnableConfirm,
            onNavigateToConfirm: onNavigateToConfirm
        )

        Spacer()
    }
    .padding(.horizontal, 24)
}

@ViewBuilder
private func PointGetOverview(balance: Int, maxAvailable: Int) -> some View {
    VStack(alignment: .center, spacing: 0) {
        Text("現在のポイント残高")
            .foregroundColor(Color("themeColor"))
            .fontWeight(.bold)

        Text(String(balance))
            .foregroundColor(Color("themeColor"))
            .font(.largeTitle)
            .fontWeight(.bold)

        Spacer().frame(height: 8)

        Text("今回獲得できるポイントの上限は\(maxAvailable)です。")
            .foregroundColor(.black)
    }
}

@ViewBuilder
private func PointGetInputField(
    inputPoint: Int,
    errorMessage: String?,
    onValueChange: @escaping (Int) -> Void
) -> some View {
    VStack(alignment: .leading, spacing: 4) {
        TextField(
            "獲得ポイント数",
            text: Binding(
                get: { inputPoint > 0 ? String(inputPoint) : "" },
                set: { newValue in
                    if newValue.isEmpty {
                        onValueChange(0)
                    } else if let intValue = Int(newValue) {
                        onValueChange(intValue)
                    }
                }
            )
        )
        .keyboardType(.numberPad)
        .textFieldStyle(RoundedBorderTextFieldStyle())
        .font(.system(size: 20))

        if let errorMessage = errorMessage {
            Text(errorMessage)
                .foregroundColor(.red)
                .font(.caption)
                .padding(.leading, 4)
        }
    }
}

@ViewBuilder
private func PointGetConfirmButton(
    isEnabled: Bool,
    onNavigateToConfirm: @escaping () -> Void
) -> some View {
    Button(action: onNavigateToConfirm) {
        Text("確認画面へ")
            .frame(maxWidth: .infinity)
            .padding()
            .background(isEnabled ? Color.blue : Color.gray)
            .foregroundColor(.white)
            .cornerRadius(8)
    }
    .disabled(!isEnabled)
}

struct PointGetInputView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            PointGetInputContents(
                viewState: .loading,
                onInputPoint: { _ in },
                onNavigateToConfirm: { _ in }
            ).previewDisplayName("ロード中")
            
            PointGetInputContents(
                viewState: .success(currentPoint: Point(balance: Int32(2000)), inputPoint: 0, errorMessage: nil, isEnableConfirm: false),
                onInputPoint: { _ in },
                onNavigateToConfirm: { _ in }
            ).previewDisplayName("初期画面")
        }
    }
}
