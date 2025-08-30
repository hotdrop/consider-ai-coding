import SwiftUI

// MARK: - SplashView
struct SplashView: View {
    let navigateToStart: () -> Void
    let navigateToHome: () -> Void

    @StateObject private var viewModel: SplashViewModel
    @State private var didTriggerInitialLoad = false
    @State private var routed = false
    
    init(
        viewModel: SplashViewModel = SplashViewModel(),
        navigateToStart: @escaping () -> Void,
        navigateToHome: @escaping () -> Void
    ) {
        _viewModel = StateObject(wrappedValue: viewModel)
        self.navigateToStart = navigateToStart
        self.navigateToHome = navigateToHome
    }

    var body: some View {
        NavigationView {
            SplashContents(
                viewState: viewModel.viewState,
                navigateToStart: navigateToStart
            )
        }
        .task {
            guard !didTriggerInitialLoad else { return }
            didTriggerInitialLoad = true
            await viewModel.load()
        }
        .onChange(of: viewModel.viewState) { state in
            guard !routed else { return }
            if case .loaded = state {
                navigateToHome()
            }
        }
    }
}

// MARK: - SplashContents
private struct SplashContents: View {
    let viewState: SplashViewState
    let navigateToStart: () -> Void
    
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
                FirstTimeView(navigateToStart: navigateToStart)
                
            case .error(let message):
                Text("\(message)").foregroundColor(.red)
            }
        }
        .navigationTitle("splash_title")
        .navigationBarTitleDisplayMode(.inline)
        .navigationViewStyle(StackNavigationViewStyle())
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
    let navigateToStart: () -> Void
    
    var body: some View {
        VStack {
            Text("splash_app_label")
                .font(.title2)
                .foregroundColor(Color("themeColor"))

            Button(action: navigateToStart) {
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
            SplashContents(
                viewState: .loading,
                navigateToStart: {}
            ).previewDisplayName("読み込み中")

            SplashContents(
                viewState: .firstTime,
                navigateToStart: {}
            ).previewDisplayName("初回起動")

            SplashContents(
                viewState: .error(message: "プレビュー用エラー"),
                navigateToStart: {}
            ).previewDisplayName("エラー")
            
            SplashContents(
                viewState: .loaded(userId: "preview-user-1234"),
                navigateToStart: {}
            ).previewDisplayName("読み込み完了")
        }
    }
}
