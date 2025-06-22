import SwiftUI
import shared

struct StartView: View {
    @StateObject private var viewModel: StartViewModel
    @State private var inputNickName: String = ""
    @State private var inputEmail: String = ""

    let onSuccess: () -> Void
    let onBack: () -> Void

    init(viewModel: StartViewModel = StartViewModel(), onSuccess: @escaping () -> Void, onBack: @escaping () -> Void) {
        _viewModel = StateObject(wrappedValue: viewModel)
        self.onSuccess = onSuccess
        self.onBack = onBack
    }

    var body: some View {
        NavigationView {
            VStack(alignment: .leading, spacing: 16) {
                Text(NSLocalizedString("start_overview", comment: ""))
                    .font(.body)
                    .padding(.bottom, 16)

                ZStack(alignment: .leading) {
                    if inputNickName.isEmpty {
                        Text(NSLocalizedString("start_nick_name_field_label", comment: ""))
                            .foregroundColor(.gray) // プレースホルダーの色を黒に
                    }
                    TextField("", text: $inputNickName)
                        .foregroundColor(.black) // 入力テキストの色を黒に
                }
                .padding(.vertical, 12)
                .padding(.horizontal, 8)
                .overlay(
                    RoundedRectangle(cornerRadius: 4)
                        .stroke(Color.black, lineWidth: 1) // 黒い枠線
                )

                ZStack(alignment: .leading) {
                    if inputEmail.isEmpty {
                        Text(NSLocalizedString("start_email_field_label", comment: ""))
                            .foregroundColor(.gray) // プレースホルダーの色を黒に
                    }
                    TextField("", text: $inputEmail)
                        .foregroundColor(.black) // 入力テキストの色を黒に
                        .keyboardType(.emailAddress)
                        .autocapitalization(.none)
                }
                .padding(.vertical, 12)
                .padding(.horizontal, 8)
                .overlay(
                    RoundedRectangle(cornerRadius: 4)
                        .stroke(Color.black, lineWidth: 1) // 黒い枠線
                )

                Button(action: {
                    Task {
                        await viewModel.registerUser(nickname: inputNickName, email: inputEmail)
                    }
                }) {
                    if case .loading = viewModel.viewState {
                        ProgressView()
                            .progressViewStyle(CircularProgressViewStyle(tint: Color("white")))
                    } else {
                        Text(NSLocalizedString("start_register_button", comment: ""))
                            .font(.headline)
                            .foregroundColor(Color("white"))
                            .padding()
                            .frame(maxWidth: .infinity)
                            .background(Color("themeColor"))
                            .cornerRadius(8)
                    }
                }
                .disabled(viewModel.viewState == .loading)
                .padding(.top, 16)

                Spacer()
            }
            .padding()
            .navigationBarTitleDisplayMode(.inline)
            .navigationBarBackButtonHidden(true)
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    Button(action: onBack) {
                        Image(systemName: "arrow.backward")
                            .foregroundColor(Color("white"))
                    }
                }
                ToolbarItem(placement: .principal) {
                    Text(NSLocalizedString("start_title", comment: ""))
                        .font(.system(size: 20))
                        .foregroundColor(Color("white"))
                }
            }
            .background(
                VStack(spacing: 0) {
                    Color("appbarColor") // Toolbarの背景色
                        .frame(height: 56) // Toolbarの高さに合わせる
                    Color("white") // コンテンツ部分の背景色
                }
                .ignoresSafeArea(edges: .all)
            )
        }
        .alert(item: Binding<ErrorAlertItem?>(
            get: {
                if case .error(let item) = viewModel.viewState {
                    return item
                }
                return nil
            },
            set: { _ in
                if case .error = viewModel.viewState {
                    viewModel.viewState = .idle
                }
            }
        )) { item in
            Alert(title: Text("エラー"), message: Text(item.message), dismissButton: .default(Text("OK")))
        }
        .onChange(of: viewModel.viewState) { newState in
            if case .success = newState {
                onSuccess()
            }
        }
    }
}

struct StartView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            StartView(viewModel: StartViewModel.mock(.idle), onSuccess: {}, onBack: {})
                .previewDisplayName("Idle State")

            StartView(viewModel: StartViewModel.mock(.loading), onSuccess: {}, onBack: {})
                .previewDisplayName("Loading State")

            StartView(viewModel: StartViewModel.mock(.success), onSuccess: {}, onBack: {})
                .previewDisplayName("Success State")

            StartView(viewModel: StartViewModel.mock(.error(ErrorAlertItem(message: "プレビューエラーメッセージ"))), onSuccess: {}, onBack: {})
                .previewDisplayName("Error State")
        }
    }
}
