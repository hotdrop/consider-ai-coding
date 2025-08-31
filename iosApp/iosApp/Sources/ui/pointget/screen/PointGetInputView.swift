import SwiftUI
import shared

struct PointGetInputView: View {
    let onClose: () -> Void
    
    // ViewModel のオーナーではないため @ObservedObject
    @ObservedObject private var viewModel: PointGetViewModel
    // 確認画面に遷移する
    @State private var isActiveConfirm: Bool = false
    
    init(
        onClose: @escaping () -> Void,
        viewModel: PointGetViewModel
    ) {
        self.onClose = onClose
        self._viewModel = ObservedObject(initialValue: viewModel)
    }

    var body: some View {
        // TODO NavigationViewをPointGetInputContentsに持っていく
        NavigationView {
            ZStack {
                // 非表示のNavigationLinkでConfirmへ遷移
                // TODO NavigationBarが表示されてしまう
                NavigationLink(
                    destination: PointGetConfirmView(
                        onClose: onClose,
                        viewModel: viewModel
                    ),
                    isActive: $isActiveConfirm
                ) { EmptyView() }
                .hidden()

                PointGetInputContents(
                    viewState: viewModel.viewState,
                    onInputPoint: { newValue in
                        viewModel.inputPoint(newInputPoint: newValue)
                    },
                    onNavigateToConfirm: { _ in
                        isActiveConfirm = true
                    }
                )
            }
            .navigationTitle("point_get_title")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                // ルート：戻る＝フローを閉じる（親に戻る）
                ToolbarItem(placement: .navigationBarLeading) {
                    Button(action: { onClose() }) {
                        Image(systemName: "chevron.backward")
                            .foregroundColor(Color("white"))
                    }
                }
            }
        }
        .navigationViewStyle(StackNavigationViewStyle()) // iOS15
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
        ZStack {
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
        Text("point_get_input_overview")
            .foregroundColor(Color("themeColor"))
            .fontWeight(.bold)

        Text(String(balance))
            .foregroundColor(Color("themeColor"))
            .font(.largeTitle)
            .fontWeight(.bold)

        Spacer().frame(height: 8)
        
        let availablePoint = Int32(maxAvailable)
        Text("point_get_input_attention \(availablePoint)")
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
            "point_get_input_text_field_label",
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
        Text("point_get_input_confirm_button")
            .frame(maxWidth: .infinity)
            .padding()
            .background(isEnabled ? Color("themeColor") : Color.gray)
            .foregroundColor(.white)
            .cornerRadius(8)
    }
    .disabled(!isEnabled)
}

// MARK: - Previews
struct PointGetInputView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            PointGetInputContents(
                viewState: .success(
                    currentPoint: Point(balance: Int32(2000)),
                    inputPoint: 0, errorMessage: nil,
                    isEnableConfirm: false
                ),
                onInputPoint: { _ in },
                onNavigateToConfirm: { _ in }
            ).previewDisplayName("初期画面")
            
            PointGetInputContents(
                viewState: .success(
                    currentPoint: Point(balance: Int32(2000)),
                    inputPoint: 1500, errorMessage: nil,
                    isEnableConfirm: true
                ),
                onInputPoint: { _ in },
                onNavigateToConfirm: { _ in }
            ).previewDisplayName("ポイント入力画面")
            
            PointGetInputContents(
                viewState: .loading,
                onInputPoint: { _ in },
                onNavigateToConfirm: { _ in }
            ).previewDisplayName("ロード中")
        }
    }
}
