import SwiftUI
import shared

struct StartView: View {
    @Environment(\.dismiss) private var dismiss
    @StateObject private var viewModel: StartViewModel
    private let onRegisterSuccess: () -> Void
    @State private var didNavigateOnSuccess = false

    init(viewModel: StartViewModel = StartViewModel(), onRegisterSuccess: @escaping () -> Void) {
        _viewModel = StateObject(wrappedValue: viewModel)
        self.onRegisterSuccess = onRegisterSuccess
    }

    var body: some View {
        StartContents(
            viewState: viewModel.viewState,
            errorAlertItem: $viewModel.errorAlertItem,
            onRegisterUser: { nickname, email async in
                await viewModel.registerUser(nickname: nickname, email: email)
            },
            onTapToolbarBackButton: {
                dismiss()
            }
        )
        .onChange(of: viewModel.viewState) { newState in
            if case .success = newState, !didNavigateOnSuccess {
                didNavigateOnSuccess = true
                onRegisterSuccess()
            }
        }
    }
}

// MARK: - StartContents
private struct StartContents: View {
    let viewState: StartViewState
    @Binding var errorAlertItem: StartErrorAlertItem?
    let onRegisterUser: (_ nickname: String, _ email: String) async -> Void
    let onTapToolbarBackButton: () -> Void
    
    @State private var inputNickname: String = ""
    @State private var inputEmail: String = ""
    
    // フォーカス制御
    @FocusState private var focusedField: Field?
    enum Field { case nickname, email }
    
    private var isRegisterDisabled: Bool {
        inputNickname.isEmpty || inputEmail.isEmpty || viewState == .loading
    }
    
    var body: some View {
        VStack(alignment: .leading, spacing: 16) {
            Text("start_overview")
                .font(.body)
                .padding(.bottom, 16)

            NicknameTextField(
                text: $inputNickname,
                onSubmitNext: {
                    focusedField = .email
                }
            ).focused($focusedField, equals: .nickname)

            EmailTextField(
                text: $inputEmail,
                onSubmitDone: {
                    guard !isRegisterDisabled else { return }
                    Task { await onRegisterUser(inputNickname, inputEmail) }
                }
            ).focused($focusedField, equals: .email)

            RegisterButton(
                isLoading: viewState == .loading,
                isDisabled: isRegisterDisabled,
                action: {
                    Task { await onRegisterUser(inputNickname, inputEmail) }
                }
            )
            .padding(.top, 16)

            Spacer()
        }
        .padding()
        .navigationTitle("start_title")
        .navigationBarTitleDisplayMode(.inline)
        .navigationBarBackButtonHidden(true)
        .toolbar {
            ToolbarItem(placement: .navigationBarLeading) {
                Button(action: onTapToolbarBackButton) {
                    Image(systemName: "arrow.backward")
                        .foregroundColor(Color("white"))
                }
            }
        }
        .alert(item: $errorAlertItem) { item in
            Alert(
                title: Text("dialog_error_title"),
                message: Text(item.message),
                dismissButton: .default(Text("dialog_ok"))
            )
        }
    }
}

// MARK: - Nickname TextField
private struct NicknameTextField: View {
    @Binding var text: String
    let onSubmitNext: () -> Void

    var body: some View {
        TextField(NSLocalizedString("start_nick_name_field_label", comment: ""),text: $text)
            .keyboardType(.default)
            .textInputAutocapitalization(.never)
            .autocorrectionDisabled(true)
            .submitLabel(.next)
            .onSubmit(onSubmitNext)
            .foregroundColor(.primary)
            .padding(.vertical, 12)
            .padding(.horizontal, 8)
            .overlay(
                RoundedRectangle(cornerRadius: 8)
                    .stroke(Color.secondary.opacity(0.25), lineWidth: 1)
            )
    }
}

// MARK: - EmailTextField
private struct EmailTextField: View {
    @Binding var text: String
    let onSubmitDone: () -> Void

    var body: some View {
        TextField(NSLocalizedString("start_email_field_label", comment: ""), text: $text)
            .keyboardType(.emailAddress)
            .textInputAutocapitalization(.never)
            .autocorrectionDisabled(true)
            .submitLabel(.next)
            .onSubmit(onSubmitDone)
            .foregroundColor(.primary)
            .padding(.vertical, 12)
            .padding(.horizontal, 8)
            .overlay(
                RoundedRectangle(cornerRadius: 8)
                    .stroke(Color.secondary.opacity(0.25), lineWidth: 1)
            )
    }
}

// MARK: - RegisterButton
private struct RegisterButton: View {
    let isLoading: Bool
    let isDisabled: Bool
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            ZStack {
                Text("start_register_button")
                    .font(.headline)
                    .opacity(isLoading ? 0 : 1)
                if isLoading {
                    ProgressView()
                        .progressViewStyle(CircularProgressViewStyle(tint: Color("white")))
                }
            }
            .foregroundColor(Color("white"))
            .frame(maxWidth: .infinity)
            .padding()
            .background(Color("themeColor"))
            .cornerRadius(8)
        }
        .disabled(isDisabled)
    }
}

// MARK: - Previews
struct StartView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            NavigationStack {
                StartContents(
                    viewState: .idle,
                    errorAlertItem: .constant(nil),
                    onRegisterUser: {_,_ in },
                    onTapToolbarBackButton: {}
                )
            }.previewDisplayName("画面初期表示")
            
            NavigationStack {
                StartContents(
                    viewState: .loading,
                    errorAlertItem: .constant(nil),
                    onRegisterUser: {_,_ in },
                    onTapToolbarBackButton: {}
                )
            }.previewDisplayName("実行中")

            NavigationStack {
                StartContents(
                    viewState: .success,
                    errorAlertItem: .constant(nil),
                    onRegisterUser: {_,_ in },
                    onTapToolbarBackButton: {}
                )
            }.previewDisplayName("成功")

            NavigationStack {
                StartContents(
                    viewState: .idle,
                    errorAlertItem: .constant(StartErrorAlertItem(message: "プレビューエラーメッセージ")),
                    onRegisterUser: {_,_ in },
                    onTapToolbarBackButton: {}
                )
            }.previewDisplayName("エラー")
        }
    }
}
