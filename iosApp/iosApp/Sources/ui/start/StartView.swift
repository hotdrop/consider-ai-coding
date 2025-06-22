import SwiftUI
import shared

struct StartView: View {
    @Environment(\.presentationMode) var presentationMode
    @StateObject private var viewModel: StartViewModel
    @State private var inputNickName: String = ""
    @State private var inputEmail: String = ""

    init(viewModel: StartViewModel = StartViewModel()) {
        _viewModel = StateObject(wrappedValue: viewModel)
    }

    var body: some View {
        NavigationView {
            VStack(alignment: .leading, spacing: 16) {
                Text(NSLocalizedString("start_overview", comment: ""))
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

                RegisterButton(isLoading: viewModel.viewState == .loading) {
                    Task {
                        await viewModel.registerUser(nickname: inputNickName, email: inputEmail)
                    }
                }
                .padding(.top, 16)

                Spacer()
            }
            .padding()
            .navigationTitle(NSLocalizedString("start_title", comment: ""))
            .navigationBarTitleDisplayMode(.inline)
            .navigationBarBackButtonHidden(true)
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    Button(action: { presentationMode.wrappedValue.dismiss() }) {
                        Image(systemName: "arrow.backward")
                            .foregroundColor(Color("white"))
                    }
                }
            }
        }
        .alert(item: $viewModel.errorAlertItem) { item in
            Alert(title: Text("エラー"), message: Text(item.message), dismissButton: .default(Text("OK")))
        }
        .onChange(of: viewModel.viewState) { newState in
            if case .success = newState {
                // TODO: 次の画面に遷移
                presentationMode.wrappedValue.dismiss()
            }
        }
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
                Text(NSLocalizedString("start_register_button", comment: ""))
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
            StartView(viewModel: StartViewModel.mock(.idle))
                .previewDisplayName("画面初期表示")

            StartView(viewModel: StartViewModel.mock(.loading))
                .previewDisplayName("実行中")

            StartView(viewModel: StartViewModel.mock(.success))
                .previewDisplayName("成功")

            StartView(viewModel: StartViewModel.mock(.idle, error: ErrorAlertItem(message: "プレビューエラーメッセージ")))
                .previewDisplayName("エラー")
        }
    }
}
