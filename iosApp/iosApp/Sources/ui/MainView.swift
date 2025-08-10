import SwiftUI

// ルートを表す値オブジェクト
private enum MainRoute: Hashable {
    case start
    case home
}

struct MainView: View {
    @StateObject private var viewModel: MainViewModel
    @State private var path = NavigationPath()
    
    init(viewModel: MainViewModel = MainViewModel()) {
        _viewModel = StateObject(wrappedValue: viewModel)
    }

    var body: some View {
        NavigationStack(path: $path) {
            VStack {
                MainContents(
                    viewState: viewModel.viewState,
                    onLoadedNavigation: {
                        path.append(MainRoute.home)
                    },
                    onFirstTimeView: {
                        path.append(MainRoute.start)
                    }
                )
            }
            .navigationTitle("splash_title")
            .navigationBarTitleDisplayMode(.inline)
            .navigationDestination(for: MainRoute.self) { route in
                switch route {
                case .start:
                    StartView() {
                        path.append(MainRoute.home)
                    }
                case .home:
                    HomeView(viewModel: HomeViewModel())
                }
            }
        }
        .task {
            await viewModel.load()
        }
    }
}

// MARK: - MainContents
private struct MainContents: View {
    let viewState: MainViewState
    let onLoadedNavigation: () -> Void
    let onFirstTimeView: () -> Void
    
    var body: some View {
        Image("start")
            .resizable()
            .aspectRatio(contentMode: .fit)
            .frame(width: 264, height: 264)
            .padding(.vertical, 32)
        
        switch viewState {
        case .loading:
            ProgressView()
                .progressViewStyle(CircularProgressViewStyle(tint: Color("themeColor")))

        case .loaded(let userId):
            LoadedView(userId: userId)
                .task(id: userId) { onLoadedNavigation() }
        
        case .firstTime:
            FirstTimeView {
                onFirstTimeView()
            }
            
        case .error(let message):
            Text("\(message)").foregroundColor(.red)
        }
    }
}

private struct LoadedView: View {
    let userId: String
    
    var body: some View {
        VStack {
            ProgressView()
                .progressViewStyle(CircularProgressViewStyle(tint: Color("themeColor")))
            
            Text("splash_user_id_label \(userId)")
                .foregroundColor(Color("themeColor"))
                .padding(.top, 64)
        }
    }
}

private struct FirstTimeView: View {
    let onStartTapped: () -> Void
    
    var body: some View {
        VStack {
            Text("splash_app_label")
                .font(.title2)
                .foregroundColor(Color("themeColor"))

            Button(action: {
                onStartTapped()
            }) {
                Text("splash_first_time_button")
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
            MainContents(
                viewState: .loading,
                onLoadedNavigation: {},
                onFirstTimeView: {}
            ).previewDisplayName("読み込み中")

            MainContents(
                viewState: .firstTime,
                onLoadedNavigation: {},
                onFirstTimeView: {}
            ).previewDisplayName("初回起動")

            MainContents(
                viewState: .error("プレビュー用エラー"),
                onLoadedNavigation: {},
                onFirstTimeView: {}
            ).previewDisplayName("エラー")
        }
    }
}
