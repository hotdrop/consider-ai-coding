import SwiftUI

struct MainView: View {
    @StateObject private var viewModel: MainViewModel
    
    init(viewModel: MainViewModel = MainViewModel()) {
        _viewModel = StateObject(wrappedValue: viewModel)
    }

    var body: some View {
        NavigationView {
            VStack {
                switch viewModel.viewState {
                case .loading:
                    ProgressView()
                        .progressViewStyle(CircularProgressViewStyle(tint: Color("primary")))
                case .loaded(let userId):
                    // TODO: HomeViewへの遷移ロジックを実装
                    Text("ユーザーID: \(userId)")
                        .font(.title)
                        .foregroundColor(Color("themeColor"))
                        .onAppear {
                            // 実際にはここでHomeViewへ遷移
                            print("Navigate to HomeView with userId: \(userId)")
                        }
                case .firstTime:
                    VStack {
                        Image("start") // Assets.xcassetsに登録された画像
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 264, height: 264)
                            .padding(.vertical, 32)

                        Text(NSLocalizedString("splash_app_label", comment: ""))
                            .font(.largeTitle)
                            .foregroundColor(Color("themeColor"))
                            .padding(.top, 32)

                        Button(action: {
                            // TODO: StartViewへの遷移ロジックを実装
                            print("Navigate to StartView")
                        }) {
                            Text(NSLocalizedString("splash_first_time_button", comment: ""))
                                .font(.headline)
                                .foregroundColor(Color("white"))
                                .padding()
                                .frame(maxWidth: .infinity)
                                .background(Color("primary"))
                                .cornerRadius(8)
                        }
                        .padding(.horizontal, 32)
                        .padding(.top, 32)
                    }
                case .error(let message):
                    Text("エラー: \(message)")
                        .foregroundColor(.red)
                }
            }
            .navigationBarHidden(true) // ツールバーはカスタムで実装するため非表示
            .toolbar {
                ToolbarItem(placement: .principal) {
                    Text(NSLocalizedString("splash_title", comment: ""))
                        .font(.system(size: 20))
                        .foregroundColor(Color("white"))
                }
            }
            .background(Color("appbarColor").ignoresSafeArea(edges: .top)) // ツールバーの背景色
            .background(Color("white").ignoresSafeArea(edges: .bottom)) // 下部の背景色
        }
    }
}

struct MainView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            MainView(viewModel: MainViewModel.mock(.loading))
                .previewDisplayName("読み込み中")

            MainView(viewModel: MainViewModel.mock(.loaded("preview_user_123")))
                .previewDisplayName("ログイン済み")

            MainView(viewModel: MainViewModel.mock(.firstTime))
                .previewDisplayName("初回起動")

            MainView(viewModel: MainViewModel.mock(.error("プレビュー用エラー")))
                .previewDisplayName("エラー")
        }
    }
}
