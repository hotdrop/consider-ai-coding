import SwiftUI
import shared

struct StartView: View {
    @Environment(\.dismiss) private var dismiss
    @StateObject private var viewModel: StartViewModel
    
    private let onRegisterSuccess: () -> Void

    init(viewModel: StartViewModel = StartViewModel(), onRegisterSuccess: @escaping () -> Void) {
        _viewModel = StateObject(wrappedValue: viewModel)
        self.onRegisterSuccess = onRegisterSuccess
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 16) {
            StartContents(
                viewState: viewModel.viewState,
                onRegisterUser: { email, password async in
                    await viewModel.registerUser(nickname: email, email: email)
                }
            )
        }
        .padding()
        .navigationTitle("start_title")
        .navigationBarTitleDisplayMode(.inline)
        .navigationBarBackButtonHidden(true)
        .toolbar {
            ToolbarItem(placement: .navigationBarLeading) {
                Button(action: { dismiss() }) {
                    Image(systemName: "arrow.backward")
                        .foregroundColor(Color("white"))
                }
            }
        }
        .alert(item: $viewModel.errorAlertItem) { item in
            Alert(title: Text("dialog_error_title"), message: Text(item.message), dismissButton: .default(Text("dialog_ok")))
        }
        .onChange(of: viewModel.viewState) { newState in
            if case .success = newState {
                onRegisterSuccess()
            }
        }
    }
}

// MARK: - StartContents
private struct StartContents: View {
    let viewState: StartViewState
    let onRegisterUser: (String, String) async -> Void
    
    @State private var inputNickName: String = ""
    @State private var inputEmail: String = ""
    
    var body: some View {
        Text("start_overview")
            .font(.body)
            .padding(.bottom, 16)

        CustomTextField(
            placeholder: NSLocalizedString("start_nick_name_field_label", comment: ""),
            text: $inputNickName
        )

        CustomTextField(
            placeholder: NSLocalizedString("start_email_field_label", comment: ""),
            text: $inputEmail,
            keyboardType: .emailAddress,
            autocapitalization: .none
        )

        RegisterButton(isLoading: viewState == .loading) {
            Task {
                await onRegisterUser(inputNickName, inputEmail)
            }
        }
        .padding(.top, 16)

        Spacer()
    }
}

private struct CustomTextField: View {
    let placeholder: String
    @Binding var text: String
    var keyboardType: UIKeyboardType = .default
    var autocapitalization: UITextAutocapitalizationType = .sentences

    var body: some View {
        TextField(placeholder, text: $text)
            .foregroundColor(.black)
            .keyboardType(keyboardType)
            .autocapitalization(autocapitalization)
            .padding(.vertical, 12)
            .padding(.horizontal, 8)
            .overlay(
                RoundedRectangle(cornerRadius: 4)
                    .stroke(Color.black, lineWidth: 1)
            )
    }
}

private struct RegisterButton: View {
    let isLoading: Bool
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            if isLoading {
                ProgressView()
                    .progressViewStyle(CircularProgressViewStyle(tint: Color("white")))
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(Color("themeColor"))
                    .cornerRadius(8)
            } else {
                Text("start_register_button")
                    .font(.headline)
                    .foregroundColor(Color("white"))
                    .padding()
                    .frame(maxWidth: .infinity)
                    .background(Color("themeColor"))
                    .cornerRadius(8)
            }
        }
        .disabled(isLoading)
    }
}

struct StartView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            StartContents(
                viewState: .idle,
                onRegisterUser: {_,_ in }
            ).previewDisplayName("画面初期表示")

            StartContents(
                viewState: .loading,
                onRegisterUser: {_,_ in }
            ).previewDisplayName("実行中")

            StartContents(
                viewState: .success,
                onRegisterUser: {_,_ in }
            ).previewDisplayName("成功")

//            StartContents(viewState: .idle, error: ErrorAlertItem(message: "プレビューエラーメッセージ")), onRegisterSuccess: {})
//                .previewDisplayName("エラー")
        }
    }
}
