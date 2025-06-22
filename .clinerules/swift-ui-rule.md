# SwiftUI View / ViewModel 実装ルール
このドキュメントは、SwiftUIアプリ開発における View および ViewModel の設計・実装方針を統一するためのルールです。AIコーディング支援ツール（Cline、Roo Code 等）がこれを読み取り、プロジェクト全体に一貫した構造を生成できるようにすることを目的とします。

## 全体方針
- 状態駆動型 UI：すべての画面は `enum` によって状態管理され、状態ごとに描画内容を切り替える。
- 責務分離：UI描画は `View` に、副作用（データ取得・ナビゲーション）は `ViewModel` に明確に分離する。
- 再利用性の確保：再利用可能なUI要素やスタイルは、`ViewModifier` やカスタム `ButtonStyle` として切り出す。
- 副作用の明示：非同期処理・ナビゲーションは `.task(id:)` もしくは親 `View` から渡されたクロージャで制御する。
- `View` に渡すクロージャ（`onSuccessTapped: () -> Void`）の意図を理解し、親Viewが副作用を管理するようコードを分割すること。

## 目指す設計の特徴
- SwiftUIらしい宣言的UI
- 責務が明確に分離された拡張性の高い構造
- Viewと副作用を分離したテスト可能な設計
- 生成コードでも即座に実戦投入できる品質

## ディレクトリ構成
iosApp/
- iOSApp.swift  # iOSアプリのエントリポイント
- IosPlatformDependencies.swift
- Sources
  - data # KMPのSharedのうちLocalDataとのIF
  - ui
    - start # 機能毎のディレクトリ。このディレクトリにViewとViewModel、Componentsを定義する
    - ... # 機能毎のディレクトリをここに作成していく
    - MainView.swift # アプリ起動時のView。このViewだけはuiディレクトリの直下におく
    - MainViewModel.swift # アプリ起動時のViewに対応したViewModel。このViewModelだけはuiディレクトリの直下に置く
  - usecase # SwiftUIのPreview用としてMockのUseCaseを使いたいため、このディレクトリにProtocolを定義する

## Viewルール
### コンポーザブルなView構成
- 複数状態に対応する `View` は `switch` による分岐処理を使い、各状態専用の View に責務を委譲する。
- 各 `View` はなるべく 単一の責務（Single Responsibility） を持たせる。

```swift
switch viewModel.viewState {
  case .loading: LoadingView()
  case .success(let data): SuccessView(data: data)
  case .error(let message): ErrorView(message: message)
}
```

### Viewの状態更新と副作用
- `onAppear`は極力避け`task(id:)`を使用して状態の変化によってのみ副作用が発生するようにする。
- 画面遷移などの副作用は、親からクロージャを受け取り、子`View`から直接行わない。
```swift
.task(id: viewModel.state) {
  if case .success(let user) = viewModel.state {
    onNavigateToHome(user)
  }
}
```
### Viewの構成ガイドライン
- レイアウト構成には`VStack`, `ZStack`, `Spacer` を使い`SafeArea`に配慮。
- 背景色は `ZStack + Color.white.ignoresSafeArea()` で一括管理。
- 色やサイズは `Color("themeColor")` のように `Asset Catalog` または定数を通して管理。

## ViewModelルール
###  責務の範囲
- ViewModelは状態 (`@Published`/`@State`) の管理と副作用の実行に責任を持つ。
- UIロジック（ボタンの色や表示文言の判定）もViewModelが担う。

### 状態管理ルール
- 画面の状態は `enum ViewState `で定義し`@Published var viewState: ViewState`で管理。
- 状態遷移は明示的に `updateState(to:)` のような関数で管理する。
```swift
enum LoginViewState {
  case idle
  case loading
  case success(User)
  case error(String)
}
```

### 非同期処理の書き方
- 非同期処理には `Task { await ... }`を使用。
- Swift Concurrency（async/await）により`ViewModel.load()`のような関数を通して非同期処理を行う。
```swift
func load() async {
  do {
    let user = try await apiService.fetchUser()
    viewState = .success(user)
  } catch {
    viewState = .error("読み込みに失敗しました")
  }
}
```

## ViewとViewModelの接続
### 標準構成
```swift
struct SomeView: View {
  @StateObject private var viewModel = SomeViewModel()

  var body: some View {
    MainContentView(state: viewModel.viewState)
      .task {
        await viewModel.load()
      }
  }
}
```

### DI対応（テストやプレビュー向け）
```swift
init(viewModel: SomeViewModel = SomeViewModel()) {
  _viewModel = StateObject(wrappedValue: viewModel)
}
```

## テスト・プレビュー対応
- 各 `View` に `PreviewProvider` を付け、`.success`, `.error` などの状態を再現できるようにする。
- `ViewModel` には `MockService` を渡して状態遷移を検証可能にする。

# SwiftUI UI Design Rules:
- Use Built-in Components: Utilize SwiftUI's native UI elements like List, NavigationView, TabView, and SF Symbols for a polished, iOS-consistent look.
- Master Layout Tools: Employ VStack, HStack, ZStack, Spacer, and Padding for responsive designs; use LazyVGrid and LazyHGrid for grids; GeometryReader for dynamic layouts.
- Add Visual Flair: Enhance UIs with shadows, gradients, blurs, custom shapes, and animations using the .animation() modifier for smooth transitions.
- Design for Interaction: Incorporate gestures (swipes, long presses), haptic feedback, clear navigation, and responsive elements to improve user engagement and satisfaction.
