import SwiftUI

// ルートを表す値オブジェクト
private enum MainRoute: Hashable {
    case start
    case home
}

// MARK: - MainView
struct MainView: View {
    @StateObject private var viewModel: MainViewModel
    
    @State private var path = NavigationPath()
    @State private var didTriggerInitialLoad = false
    @State private var hasNavigatedToHome = false
    @State private var hasNavigatedToStart = false
    
    init(viewModel: MainViewModel = MainViewModel()) {
        _viewModel = StateObject(wrappedValue: viewModel)
    }

    var body: some View {
        NavigationStack(path: $path) {
            MainContents(
                viewState: viewModel.viewState,
                onStartRequested: {
                    if !hasNavigatedToStart {
                        path.append(MainRoute.start)
                        hasNavigatedToStart = true
                    }
                }
            )
            .navigationDestination(for: MainRoute.self) { route in
                switch route {
                case .start:
                    StartView(onRegisterSuccess: {
                        if !hasNavigatedToHome {
                            path.append(MainRoute.home)
                            hasNavigatedToHome = true
                        }
                    })
                case .home:
                    HomeView(viewModel: HomeViewModel())
                }
            }
        }
        .task {
            guard !didTriggerInitialLoad else { return }
            didTriggerInitialLoad = true
            await viewModel.load()
        }
        .onChange(of: viewModel.viewState) { state in
            switch state {
            case .loaded:
                if !hasNavigatedToHome {
                    path.append(MainRoute.home)
                    hasNavigatedToHome = true
                }
            default:
                break
            }
        }
    }
}

// MARK: - MainContents
private struct MainContents: View {
    let viewState: MainViewState
    let onStartRequested: () -> Void
    
    var body: some View {
        VStack {
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
            
            case .firstTime:
                FirstTimeView(onStartRequested: onStartRequested)
                
            case .error(let message):
                Text("\(message)").foregroundColor(.red)
            }
        }
        .navigationTitle("splash_title")
        .navigationBarTitleDisplayMode(.inline)
    }
}

// MARK: - LoadedView
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

// MARK: - FirstTimeView
private struct FirstTimeView: View {
    let onStartRequested: () -> Void
    
    var body: some View {
        VStack {
            Text("splash_app_label")
                .font(.title2)
                .foregroundColor(Color("themeColor"))

            Button(action: onStartRequested) {
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

// MARK: - Previews
struct MainView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            NavigationStack {
                MainContents(
                    viewState: .loading,
                    onStartRequested: {}
                )
            }.previewDisplayName("読み込み中")

            NavigationStack {
                MainContents(
                    viewState: .firstTime,
                    onStartRequested: {}
                )
            }.previewDisplayName("初回起動")

            NavigationStack {
                MainContents(
                    viewState: .error("プレビュー用エラー"),
                    onStartRequested: {}
                )
            }.previewDisplayName("エラー")
            
            NavigationStack {
                MainContents(
                    viewState: .loaded("preview-user-1234"),
                    onStartRequested: {}
                )
            }.previewDisplayName("読み込み完了")
        }
    }
}
