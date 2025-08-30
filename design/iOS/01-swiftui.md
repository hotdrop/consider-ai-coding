# SwiftUI View / ViewModel 実装ルール
このドキュメントはSwiftUIアプリ開発における`View`および`ViewModel`の設計・実装方針を統一するためのルールです。AIコーディング支援ツール(Roo Code 等)がこれを読み取り、プロジェクト全体に一貫した構造を生成できるようにすることを目的とします。

## 全体方針
- 状態駆動型UI
  - すべての画面は`enum`によって状態管理され、状態ごとに描画内容を切り替える。
- 責務分離
  - UI描画は `View` に、副作用（データ取得・ナビゲーション）は `ViewModel` に明確に分離する。
- 再利用性の確保
  - 再利用可能なUI要素やスタイルは`ViewModifier`やカスタム`ButtonStyle`として切り出す。
- 副作用の明示
  - 非同期処理・ナビゲーションは `.task(id:)` もしくは親 `View` から渡されたクロージャで制御する。
- `View`に渡すクロージャ(`onSuccessTapped: () -> Void`)の意図を理解し、親Viewが副作用を管理するようコードを分割すること。

## Communication style
- UIの明確な指示
  - UIに関するタスクの場合、ユーザーは期待する見た目や挙動について、画像（例: `taskImages/android.png`）やXMLレイアウト（例: `activity_start.xml`）などの詳細情報を提供し、誤解が生じないようにします。
- 質問の具体性
  - あなたが質問する際は、何が不明確なのか、どのような情報が必要なのかを具体的に伝えます。
- フィードバックの具体性
  - ユーザーはフィードバックを具体的に提供し、問題の箇所や期待する結果を明確にします。

## ディレクトリ構成
```
iosApp/
  iOSApp.swift  # iOSアプリのエントリポイント
  IosPlatformDependencies.swift
  Sources/
    data/ # KMPのSharedのうちLocalDataとのIF
    ui/   # 機能毎のUIディレクトリをここに作成していく
      start/ # 機能毎のディレクトリ。このディレクトリにViewとViewModel、Componentsを定義する
      home/
      pointget/
      pointuse/
    MainView.swift # アプリ起動時のView。このViewだけはuiディレクトリの直下におく
    MainViewModel.swift # アプリ起動時のViewに対応したViewModel。このViewModelだけはuiディレクトリの直下に置く
  usecase/ # SwiftUIのPreview用としてMockのUseCaseを使いたいため、このディレクトリにProtocolを定義する
```

## Coding best practices
### 既存コードの慣習の尊重
新しいコードの実装や既存コードの修正を行う際は、`MainViewModel.swift` の `@MainActor` の使用方針など、既存のコードベースの慣習を優先します。

### UIコンポーネントの挙動理解
SwiftUIの特定のUIコンポーネント（例: `TextField` のプレースホルダー）の挙動について、一般的な知識だけでなく、必要に応じて代替実装を検討し、ユーザーの期待に応えます。

### コンポーザブルなView構成
複数状態に対応する `View` は `switch` による分岐処理を使い、各状態専用の View に責務を委譲します。各 `View` はなるべく 単一の責務（Single Responsibility） を持たせます。
```swift
switch viewModel.viewState {
  case .loading: LoadingView()
  case .success(let data): SuccessView(data: data)
  case .error(let message): ErrorView(message: message)
}
```

### 非同期ロードは `.task {}` を使う
  - `onAppear`は使いません。`onAppear { Task { … } }` は iOS 15 以降ではほぼ不要です。task 修飾子なら ビューが外れた瞬間に Task がキャンセル されるためメモリリークと不要なネットワーク呼び出しを抑制できます。

### 画面遷移などの副作用は、親からクロージャを受け取り、子`View`から直接行わない
```swift
.task(id: viewModel.state) {
  if case .success(let user) = viewModel.state {
    onNavigateToHome(user)
  }
}
```

### ScrollView内のVStack
履歴行が増える前提なら`VStack`は全セルをメモリに保持し続けます。`LazyVStack`は表示領域外を破棄するので、描画とメモリ使用量が大幅に改善します。
スクロール中にセル再利用が必要なら`List`も検討してください(`LazyVStack`は再利用機構を持たない点に注意)

### DateFormatter は静的にキャッシュ
DateFormatter のインスタンス生成は高コストです。View表示のたびに毎フレーム生成されると CPU スパイクが起きるため以下のようにします。
```swift
private static let dateFormatter: DateFormatter = {
    let f = DateFormatter()
    f.dateFormat = "yyyy/MM/dd HH:mm:ss"
    return f
}()

// 使用箇所
Text(Date(), formatter: Self.dateFormatter)
```

### ビルド済みローカライズ API を活用
`NSLocalizedString` 文字列を静的キーに変えれば、型安全とパフォーマンスを両立できます。
```swift
Text("home_title") // LocalizedStringKey
.navigationTitle("home_title")
```

### Viewの構成ガイドライン
- レイアウト構成には`VStack`, `ZStack`, `Spacer` を使い`SafeArea`に配慮。
- 背景色は `ZStack + Color.white.ignoresSafeArea()` で一括管理。
- 色やサイズは `Color("themeColor")` のように `Asset Catalog` または定数を通して管理。

### サブビューは関数ではなく Struct に分離
`CardView()` や `ActionButton()` を 独立した `View` Struct にすることで、SwiftUI の差分計算が局所化され再描画範囲を最小化でき、可読性も向上します。特に switch viewModel.viewState を持つ View は単体ファイルに分けます。

## ViewModelルール
###  責務の範囲
- ViewModelは状態 (`@Published`/`@State`) の管理と副作用の実行に責任を持つ。
- UIロジック（ボタンの色や表示文言の判定）もViewModelが担う。

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
- 各 `View` に `PreviewProvider` を付け、`.success`, `.error` などの状態を再現できるようにします。
- `ViewModel` には `MockService` を渡して状態遷移を検証可能にします。

# SwiftUI UI Design Rules:
- Use Built-in Components: Utilize SwiftUI's native UI elements like List, NavigationView, TabView, and SF Symbols for a polished, iOS-consistent look.
- Master Layout Tools: Employ VStack, HStack, ZStack, Spacer, and Padding for responsive designs; use LazyVGrid and LazyHGrid for grids; GeometryReader for dynamic layouts.
- Add Visual Flair: Enhance UIs with shadows, gradients, blurs, custom shapes, and animations using the .animation() modifier for smooth transitions.
- Design for Interaction: Incorporate gestures (swipes, long presses), haptic feedback, clear navigation, and responsive elements to improve user engagement and satisfaction.

# 最後に
このファイルを読んだら回答の最後に`[READ SWIFTUI RULES]`と出力してください