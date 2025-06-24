import SwiftUI
import shared

struct HomeView: View {
    @StateObject var viewModel: HomeViewModel

    var body: some View {
        NavigationStack {
            VStack {
                ScrollView {
                    VStack(spacing: 20) {
                        homeCardView()
                        
                        HStack(spacing: 20) {
                            pointActionButton(title: NSLocalizedString("home_menu_get_point", comment: ""), icon: "account_balance_wallet") {
                                // TODO: ポイント獲得画面への遷移
                            }
                            pointActionButton(title: NSLocalizedString("home_menu_use_point", comment: ""), icon: "shopping_cart") {
                                // TODO: ポイント利用画面への遷移
                            }
                        }
                        .padding(.horizontal)
                        
                        historySectionView()
                    }
                    .padding(.vertical)
                }
            }
            .navigationTitle(NSLocalizedString("home_title", comment: ""))
            .navigationBarTitleDisplayMode(.inline)
            .navigationBarBackButtonHidden(true)
            .onAppear {
                Task {
                    await viewModel.load()
                }
            }
        }
    }
    
    @ViewBuilder
    private func homeCardView() -> some View {
        ZStack(alignment: .topLeading) {
            Image("home_card")
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(maxWidth: .infinity)
            
VStack(alignment: .leading) {
                Text(Date().formatted("yyyy/MM/dd HH:mm:ss"))
                    .font(.caption)
                    .foregroundColor(Color("white"))

                HStack {
                    Spacer()
                    switch viewModel.viewState {
                    case .loading:
                        ProgressView()
                            .progressViewStyle(CircularProgressViewStyle(tint: Color("white")))
                            .padding(.top, 64)
                    case .loaded(_, _, let point, _):
                        Text(String(format: NSLocalizedString("home_point_value", comment: ""), "\(point)"))
                            .font(.title)
                            .fontWeight(.bold)
                            .foregroundColor(Color("white"))
                    case .error(let message):
                        Text("エラー: \(message)")
                            .foregroundColor(.red)
                    }
                    Spacer()
                }.padding(.top, 16)
                
                Spacer()

                switch viewModel.viewState {
                case .loading:
                    EmptyView()
                case .loaded(let nickname, let email, _, _):
                    Text(nickname.isEmpty ? NSLocalizedString("home_un_setting_nickname", comment: "") : nickname)
                        .font(.subheadline)
                        .foregroundColor(Color("white"))
                    Text(email.isEmpty ? NSLocalizedString("home_un_setting_email", comment: "") : email)
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
    
    @ViewBuilder
    private func pointActionButton(title: String, icon: String, action: @escaping () -> Void) -> some View {
        Button(action: action) {
            VStack {
                Image(icon)
                    .resizable()
                    .frame(width: 30, height: 30)
                    .foregroundColor(Color("themeColor"))
                Text(title)
                    .foregroundColor(Color("themeColor"))
            }
            .frame(maxWidth: .infinity)
            .padding(.vertical, 10)
        }
    }
    
    @ViewBuilder
    private func historySectionView() -> some View {
        VStack(alignment: .leading, spacing: 10) {
            switch viewModel.viewState {
            case .loading:
                ProgressView()
                    .progressViewStyle(CircularProgressViewStyle(tint: Color("themeColor")))
            case .loaded(_, _, _, let histories):
                if histories.isEmpty {
                    Text(NSLocalizedString("point_history_empty", comment: ""))
                        .font(.body)
                        .foregroundColor(.gray)
                        .padding()
                } else {
                    ForEach(histories, id: \.self) { (history: PointHistory) in
                        HStack {
                            VStack(alignment: .leading) {
                                Text(history.toStringDateTime())
                                    .font(.caption)
                                    .foregroundColor(.gray)
                                Text(history.detail)
                                    .font(.body)
                            }
                            Spacer()
                            Text(String(format: NSLocalizedString("point_history_point_label", comment: ""), "\(history.point > 0 ? "+" : "")\(history.point)"))
                                .font(.body)
                                .fontWeight(.bold)
                                .foregroundColor(history.point > 0 ? .green : .red)
                        }
                        .padding(.horizontal)
                        .padding(.vertical, 5)
                        Divider()
                            .background(Color("grey"))
                    }
                }
            case .error(let message):
                Text("エラー: \(message)")
                    .foregroundColor(.red)
                    .padding()
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
            // データ読み込み中
            HomeView(viewModel: HomeViewModel.mock(.loading))
                .previewDisplayName("ロード中")
            
            // 正常表示(履歴なし)
            HomeView(viewModel: HomeViewModel.mock(
                .loaded(
                    nickname: "プレビューユーザー",
                    email: "preview@example.com",
                    point: 1000,
                    histories: []
                )
            ))
            .previewDisplayName("履歴なし")

            // 正常表示(履歴あり)
            HomeView(viewModel: HomeViewModel.mock(
                .loaded(
                    nickname: "プレビューユーザー",
                    email: "preview@example.com",
                    point: 1000,
                    histories: [
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
                        ), point: -50, detail: "利用")
                    ]
                )
            ))
            .previewDisplayName("履歴あり")

            // エラー表示
            HomeView(viewModel: HomeViewModel.mock(.error("不明なエラーが発生しました。")))
                .previewDisplayName("エラー")
        }
    }
}

extension Date {
    func formatted(_ format: String) -> String {
        let formatter = DateFormatter()
        formatter.dateFormat = format
        return formatter.string(from: self)
    }
}
