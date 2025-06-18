import SwiftUI

// Identifiableなアラートアイテムを定義
struct AlertItem: Identifiable {
    let id = UUID()
    let message: String
}

struct StartView: View {
    @StateObject private var viewModel = StartViewModel()
    @Environment(\.presentationMode) var presentationMode // 画面を閉じるために使用

    var body: some View {
        NavigationView {
            VStack(alignment: .leading, spacing: 16) {
                Text(NSLocalizedString("start_overview", comment: ""))
                    .font(.body)
                    .padding(.bottom, 16)

                TextField(NSLocalizedString("start_nick_name_field_label", comment: ""), text: $viewModel.uiState.inputNickName)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .onChange(of: viewModel.uiState.inputNickName) { newValue in
                        viewModel.onNickNameChanged(newValue: newValue)
                    }

                TextField(NSLocalizedString("start_email_field_label", comment: ""), text: $viewModel.uiState.inputEmail)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .keyboardType(.emailAddress)
                    .autocapitalization(.none)
                    .onChange(of: viewModel.uiState.inputEmail) { newValue in
                        viewModel.onEmailChanged(newValue: newValue)
                    }

                Button(action: {
                    viewModel.save()
                }) {
                    if viewModel.uiState.isLoading {
                        ProgressView()
                            .progressViewStyle(CircularProgressViewStyle(tint: Color("white")))
                    } else {
                        Text(NSLocalizedString("start_register_button", comment: ""))
                            .font(.headline)
                            .foregroundColor(Color("white"))
                            .padding()
                            .frame(maxWidth: .infinity)
                            .background(Color("primary"))
                            .cornerRadius(8)
                    }
                }
                .disabled(viewModel.uiState.isLoading) // ローディング中はボタンを無効化
                .padding(.top, 16)

                Spacer() // 残りのスペースを埋める

            }
            .padding()
            .navigationBarHidden(true) // ツールバーはカスタムで実装するため非表示
            .toolbar {
                ToolbarItem(placement: .principal) {
                    Text(NSLocalizedString("start_title", comment: ""))
                        .font(.system(size: 20))
                        .foregroundColor(Color("white"))
                }
                ToolbarItem(placement: .navigationBarLeading) {
                    Button(action: {
                        presentationMode.wrappedValue.dismiss()
                    }) {
                        Image(systemName: "arrow.backward") // 戻るボタンのアイコン
                            .foregroundColor(Color("white"))
                    }
                }
            }
            .background(Color("appbarColor").ignoresSafeArea(edges: .top)) // ツールバーの背景色
            .background(Color("white").ignoresSafeArea(edges: .bottom)) // 下部の背景色
            .alert(item: $viewModel.errorMessage) { alertItem in
                Alert(title: Text("エラー"), message: Text(alertItem.message), dismissButton: .default(Text("OK")))
            }
            .onChange(of: viewModel.uiState.isComplete) { isComplete in
                if isComplete {
                    // TODO: HomeViewへの遷移ロジックを実装
                    print("Navigate to HomeView after registration")
                    presentationMode.wrappedValue.dismiss() // 画面を閉じる
                }
            }
        }
    }
}

struct StartView_Previews: PreviewProvider {
    static var previews: some View {
        StartView()
    }
}
