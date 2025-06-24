import SwiftUI

struct MainView: View {
    @StateObject private var viewModel: MainViewModel
    @State private var showStartView: Bool = false
    @State private var showHomeView: Bool = false
    
    init(viewModel: MainViewModel = MainViewModel()) {
        _viewModel = StateObject(wrappedValue: viewModel)
    }

    var body: some View {
        NavigationView {
            VStack {
                Image("start")
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 264, height: 264)
                    .padding(.vertical, 32)
                
                switch viewModel.viewState {
                case .loading:
                    ProgressView()
                        .progressViewStyle(CircularProgressViewStyle(tint: Color("themeColor")))
                case .loaded(let userId):
                    LoadedView(userId: userId)
                        .task(id: userId) {
                            // onAppearはViewが再描画されるたびに実行されるため、idが変化した時だけ遷移処理を実行する
                            showHomeView = true
                        }
                case .firstTime:
                    FirstTimeView {
                        showStartView = true
                    }
                case .error(let message):
                    Text("エラー: \(message)")
                        .foregroundColor(.red)
                }
            }
            .navigationTitle(NSLocalizedString("splash_title", comment: ""))
            .navigationBarTitleDisplayMode(.inline)
            .background(
                NavigationLink(
                    destination: StartView(),
                    isActive: $showStartView,
                    label: { EmptyView() }
                )
                .hidden()
            )
            .background(
                NavigationLink(
                    destination: HomeView(viewModel: HomeViewModel()),
                    isActive: $showHomeView,
                    label: { EmptyView() }
                )
                .hidden()
            )
        }
        .task {
            await viewModel.load()
        }
    }
}

private struct LoadedView: View {
    let userId: String
    
    var body: some View {
        VStack {
            ProgressView()
                .progressViewStyle(CircularProgressViewStyle(tint: Color("themeColor")))
            
            Text("ユーザーID: \(userId)")
                .foregroundColor(Color("themeColor"))
                .padding(.top, 64)
        }
    }
}

private struct FirstTimeView: View {
    let onStartTapped: () -> Void
    
    var body: some View {
        VStack {
            Text(NSLocalizedString("splash_app_label", comment: ""))
                .font(.title2)
                .foregroundColor(Color("themeColor"))

            Button(action: {
                onStartTapped()
            }) {
                Text(NSLocalizedString("splash_first_time_button", comment: ""))
                    .font(.headline)
                    .foregroundColor(Color("white"))
                    .padding()
                    .frame(maxWidth: .infinity)
                    .background(Color("themeColor"))
                    .cornerRadius(8)
            }
            .padding(.horizontal, 32)
            .padding(.top, 64)
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
