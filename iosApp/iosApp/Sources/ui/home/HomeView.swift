import SwiftUI
import shared

struct HomeView: View {
    @StateObject var viewModel: HomeViewModel

    var body: some View {
        NavigationStack {
            VStack {
                toolbarView()
                
                ScrollView {
                    VStack(spacing: 20) {
                        homeCardView()
                        
                        HStack(spacing: 20) {
                            pointActionButton(title: NSLocalizedString("home_menu_get_point", comment: ""), icon: "plus.circle.fill") {
                                // TODO: ポイント獲得画面への遷移
                            }
                            pointActionButton(title: NSLocalizedString("home_menu_use_point", comment: ""), icon: "minus.circle.fill") {
                                // TODO: ポイント利用画面への遷移
                            }
                        }
                        .padding(.horizontal)
                        
                        historySectionView()
                    }
                    .padding(.vertical)
                }
            }
            .onAppear {
                Task {
                    await viewModel.load()
                }
            }
            .background(Color("appbarColor"))
            .navigationBarHidden(true)
        }
    }
    
    @ViewBuilder
    private func toolbarView() -> some View {
        HStack {
            Text(NSLocalizedString("home_title", comment: ""))
                .font(.title2)
                .fontWeight(.bold)
                .foregroundColor(Color("white"))
            Spacer()
            Text(Date().formatted("yyyy/MM/dd HH:mm:ss")) // 現在時刻を動的に表示
                .font(.caption)
                .foregroundColor(Color("white"))
        }
        .padding()
        .background(Color("appbarColor"))
    }
    
    @ViewBuilder
    private func homeCardView() -> some View {
        ZStack(alignment: .topLeading) {
            Image("home_card")
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(maxWidth: .infinity)
            
            VStack(alignment: .leading, spacing: 10) {
                if let viewState = viewModel.viewState {
                    Text(viewState.nickname.isEmpty ? NSLocalizedString("home_un_setting_nickname", comment: "") : viewState.nickname)
                        .font(.title3)
                        .fontWeight(.bold)
                        .foregroundColor(Color("white"))
                    Text(viewState.email.isEmpty ? NSLocalizedString("home_un_setting_email", comment: "") : viewState.email)
                        .font(.subheadline)
                        .foregroundColor(Color("white"))
                    
                    Spacer()
                    
                    HStack {
                        Image(systemName: "dollarsign.circle.fill")
                            .foregroundColor(Color("white"))
                        Text(String(format: NSLocalizedString("home_point_value", comment: ""), "\(viewState.point)"))
                            .font(.headline)
                            .fontWeight(.bold)
                            .foregroundColor(Color("white"))
                    }
                } else if let error = viewModel.error {
                    switch error {
                    case .appSettingNotInitialized:
                        Text(NSLocalizedString("home_error_app_setting_not_initialized", comment: ""))
                            .foregroundColor(.red)
                    case .unknown(let message):
                        Text("エラー: \(message)")
                            .foregroundColor(.red)
                    }
                } else {
                    ProgressView()
                        .progressViewStyle(CircularProgressViewStyle(tint: Color("white")))
                }
            }
            .padding(20)
        }
        .padding(.horizontal)
    }
    
    @ViewBuilder
    private func pointActionButton(title: String, icon: String, action: @escaping () -> Void) -> some View {
        Button(action: action) {
            VStack {
                Image(systemName: icon)
                    .resizable()
                    .frame(width: 40, height: 40)
                    .foregroundColor(Color("themeColor"))
                Text(title)
                    .font(.caption)
                    .foregroundColor(Color("themeColor"))
            }
            .frame(maxWidth: .infinity)
            .padding(.vertical, 10)
            .background(Color("white"))
            .cornerRadius(10)
            .shadow(radius: 5)
        }
    }
    
    @ViewBuilder
    private func historySectionView() -> some View {
        VStack(alignment: .leading, spacing: 10) {
            Text(NSLocalizedString("point_history_title", comment: ""))
                .font(.headline)
                .padding(.horizontal)
            
            Divider()
                .background(Color("grey"))
            
            if let viewState = viewModel.viewState {
                if viewState.histories.isEmpty {
                    Text(NSLocalizedString("point_history_empty", comment: ""))
                        .font(.body)
                        .foregroundColor(.gray)
                        .padding()
                } else {
                    ForEach(viewState.histories, id: \.self) { history in
                        HStack {
                            VStack(alignment: .leading) {
                                Text(history.date.formatted("yyyy/MM/dd HH:mm:ss"))
                                    .font(.caption)
                                    .foregroundColor(.gray)
                                Text(history.type == HistoryType.acquire ? NSLocalizedString("point_history_acquire", comment: "") : NSLocalizedString("point_history_use", comment: ""))
                                    .font(.body)
                            }
                            Spacer()
                            Text(String(format: NSLocalizedString("point_history_point_label", comment: ""), "\(history.amount > 0 ? "+" : "")\(history.amount)"))
                                .font(.body)
                                .fontWeight(.bold)
                                .foregroundColor(history.amount > 0 ? .green : .red)
                        }
                        .padding(.horizontal)
                        .padding(.vertical, 5)
                        Divider()
                            .background(Color("grey"))
                    }
                }
            } else {
                ProgressView()
                    .progressViewStyle(CircularProgressViewStyle(tint: Color("themeColor")))
            }
        }
        .background(Color("white"))
        .cornerRadius(10)
        .shadow(radius: 5)
        .padding(.horizontal)
    }
}

struct HomeView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            // 1. データ読み込み中
            HomeView(viewModel: HomeViewModel.mock(nil, error: nil))
                .previewDisplayName("Loading State")

            // 2. 正常表示
            HomeView(viewModel: HomeViewModel.mock(
                HomeViewState(
                    nickname: "プレビューユーザー",
                    email: "preview@example.com",
                    point: 1000,
                    histories: [
                        History(id: 1, amount: 100, type: HistoryType.acquire, date: Date().toKotlinInstant()),
                        History(id: 2, amount: -50, type: HistoryType.use, date: Date().toKotlinInstant())
                    ]
                )
            ))
            .previewDisplayName("Content State")

            // 3. エラー表示 (AppSettingNotInitialized)
            HomeView(viewModel: HomeViewModel.mock(nil, error: .appSettingNotInitialized))
                .previewDisplayName("Error: AppSetting Not Initialized")

            // 4. エラー表示 (Unknown Error)
            HomeView(viewModel: HomeViewModel.mock(nil, error: .unknown("不明なエラーが発生しました。")))
                .previewDisplayName("Error: Unknown")
        }
    }
}

extension Date {
    func toKotlinInstant() -> shared.Instant {
        let epochMilliseconds = Int64(self.timeIntervalSince1970 * 1000)
        return shared.Instant(epochMilliseconds: epochMilliseconds)
    }
}

extension Date {
    func formatted(_ format: String) -> String {
        let formatter = DateFormatter()
        formatter.dateFormat = format
        return formatter.string(from: self)
    }
}