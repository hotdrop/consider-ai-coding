import SwiftUI
import shared

struct HomeView: View {
    @StateObject var viewModel: HomeViewModel
    
    private static let dateFormatter: DateFormatter = {
        let f = DateFormatter()
        f.dateFormat = "yyyy/MM/dd HH:mm:ss"
        return f
    }()
    
    var body: some View {
        ScrollView {
            LazyVStack(spacing: 20) {
                HomeCardView(
                    viewState: viewModel.viewState,
                    dateFormatter: Self.dateFormatter
                )
                
                HStack(spacing: 20) {
                    PointActionButton(
                        titleKey: "home_menu_get_point",
                        icon: "account_balance_wallet"
                    ) {
                        // TODO: ポイント獲得画面への遷移
                    }
                    PointActionButton(
                        titleKey: "home_menu_use_point",
                        icon: "shopping_cart"
                    ) {
                        // TODO: ポイント利用画面への遷移
                    }
                }
                .padding(.horizontal)
                
                HistorySectionView(
                    historyState: viewModel.historyState
                )
            }
            .padding(.vertical)
        }
        .navigationTitle("home_title")
        .navigationBarTitleDisplayMode(.inline)
        .navigationBarBackButtonHidden(true)
        .task {
            await viewModel.load()
        }
    }
}

// MARK: - HomeCardView

private struct HomeCardView: View {
    let viewState: HomeViewState
    let dateFormatter: DateFormatter
    
    var body: some View {
        ZStack(alignment: .topLeading) {
            Image("home_card")
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(maxWidth: .infinity)
            
            VStack(alignment: .leading) {
                Text(Date(), formatter: dateFormatter)
                    .font(.caption)
                    .foregroundColor(Color("white"))

                HStack {
                    Spacer()
                    switch viewState {
                    case .initialLoading:
                        ProgressView()
                            .progressViewStyle(CircularProgressViewStyle(tint: Color("white")))
                            .padding(.top, 64)
                    case .loaded(_, _, let point):
                        Text("home_point_value \(point)")
                            .font(.title)
                            .fontWeight(.bold)
                            .foregroundColor(Color("white"))
                    case .error(let message):
                        Text("\(message)")
                            .foregroundColor(.red)
                    }
                    Spacer()
                }.padding(.top, 16)
                
                Spacer()

                switch viewState {
                case .initialLoading:
                    EmptyView()
                case .loaded(let nickname, let email, _):
                    Text(nickname.isEmpty ? "home_un_setting_nickname" : nickname)
                        .font(.subheadline)
                        .foregroundColor(Color("white"))
                    Text(email.isEmpty ? "home_un_setting_email" : email)
                        .font(.subheadline)
                        .foregroundColor(Color("white"))
                case .error(_):
                    EmptyView()
                }
            }
            .padding(20)
        }
        .padding(.horizontal)
    }
}

// MARK: - PointActionButton

private struct PointActionButton: View {
    let titleKey: LocalizedStringKey
    let icon: String
    let action: () -> Void
    
    var body: some View {
        Button(action: action) {
            VStack {
                Image(icon)
                    .resizable()
                    .frame(width: 30, height: 30)
                    .foregroundColor(Color("themeColor"))
                Text(titleKey)
                    .foregroundColor(Color("themeColor"))
            }
            .frame(maxWidth: .infinity)
            .padding(.vertical, 10)
        }
        .buttonStyle(.plain)
        .tint(Color("themeColor"))
    }
}

// MARK: - HistorySectionView

private struct HistorySectionView: View {
    let historyState: HistoryState
    
    var body: some View {
        VStack(alignment: .leading, spacing: 10) {
            switch historyState {
            case .loading:
                ProgressView()
                    .tint(Color("themeColor"))
            case .loaded(let histories):
                if histories.isEmpty {
                    EmptyView()
                } else {
                    Divider().background(Color("grey"))
                    ForEach(histories, id: \.self) { history in
                        HistoryRow(history: history)
                        Divider().background(Color("grey"))
                    }
                }
            case .error(let message):
                Text("\(message)")
                    .foregroundColor(.red)
                    .padding()
            }
        }
        .padding(.horizontal)
    }
}

// MARK: - HistoryRow

private struct HistoryRow: View {
    let history: PointHistory

    var body: some View {
        HStack {
            VStack(alignment: .leading) {
                Text(history.toStringDateTime())
                    .font(.caption)
                    .foregroundColor(.gray)
                Text(history.detail)
                    .font(.body)
            }
            Spacer()
            Text("point_history_point_label \(history.point)")
                .font(.body)
                .fontWeight(.bold)
        }
        .padding(.horizontal)
        .padding(.vertical, 5)
    }
}

// MARK: - HomeView_Previews
 
struct HomeView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            // データ読み込み中
            // 初回ロード中
            HomeView(viewModel: HomeViewModel.mock(
                viewState: .initialLoading,
                historyState: .loading
            ))
            .previewDisplayName("初回ロード中")

            // 履歴ロード中
            HomeView(viewModel: HomeViewModel.mock(
                viewState: .loaded(nickname: "プレビューユーザー", email: "preview@example.com", point: 1000),
                historyState: .loading
            ))
            .previewDisplayName("履歴ロード中")
            
            // 正常表示(履歴なし)
            HomeView(viewModel: HomeViewModel.mock(
                viewState: .loaded(nickname: "プレビューユーザー", email: "preview@example.com", point: 1000),
                historyState: .loaded([])
            ))
            .previewDisplayName("履歴なし")

            // 正常表示(履歴あり)
            HomeView(viewModel: HomeViewModel.mock(
                viewState: .loaded(nickname: "プレビューユーザー", email: "preview@example.com", point: 1000),
                historyState: .loaded([
                    PointHistory(dateTime: Kotlinx_datetimeLocalDateTime(
                        year: 2025,
                        monthNumber: 6,
                        dayOfMonth: 21,
                        hour: 10,
                        minute: 20,
                        second: 10,
                        nanosecond: 0
                    ), point: 100, detail: "獲得"),
                    PointHistory(dateTime: Kotlinx_datetimeLocalDateTime(
                        year: 2025,
                        monthNumber: 6,
                        dayOfMonth: 22,
                        hour: 15,
                        minute: 25,
                        second: 20,
                        nanosecond: 0
                    ), point: 50, detail: "利用"),
                    PointHistory(dateTime: Kotlinx_datetimeLocalDateTime(
                        year: 2025,
                        monthNumber: 6,
                        dayOfMonth: 22,
                        hour: 18,
                        minute: 32,
                        second: 56,
                        nanosecond: 0
                    ), point: 200, detail: "利用")
                ])
            ))
            .previewDisplayName("履歴あり")

            // エラー表示
            HomeView(viewModel: HomeViewModel.mock(
                viewState: .error("不明なエラーが発生しました。"),
                historyState: .error("履歴の読み込みに失敗しました。")
            ))
            .previewDisplayName("エラー")
        }
    }
}
