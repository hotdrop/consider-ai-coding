# Active Context

## 必ず守ること(編集禁止)
現在のタスクに書かれた内容は必ず1つずつ「Plan/Act」を経てユーザーに確認しながら実装すること。Planで実装計画を立てた後、Actで実装に入った場合「現在のタスク」に列挙されたタスクを一度に全部こなしてはいけません。必ず1つずつPlan→Actを行い、1つ完了したらユーザーに確認し、Planで再び実装計画から行ってください。

## 現在のタスク
`StartView.swift` と `StartViewModel.swift` のリファクタリング

### 計画

#### Step 1: `StartViewModel.swift` のリファクタリング
*   UIの状態を `enum ViewState { case idle, loading, success, error(String) }` で一元管理する。
*   テスト容易性を考慮し、DI（依存性注入）可能な `init` を用意する。
*   `registerUser(nickname:email:)` メソッドに非同期処理と状態更新ロジックを集約する。
*   `@MainActor` アノテーションを削除し、`MainViewModel.swift` と同様に `await MainActor.run { ... }` を使用してメインスレッドでの状態更新を行うようにした。
*   プレビューやテストのために、`mock` extension と `DummyAppSettingUseCase` を追加した。
→ **完了済み**

#### Step 2: `StartView.swift` のリファクタリング
1.  **背景色の修正:**
    *   `NavigationView` 全体の背景色指定を見直し、`Toolbar` 部分の背景が `appbarColor` に、コンテンツ部分の背景が `white` になるように修正した。
    *   既存の誤った `.background()` 指定を削除した。
2.  **`Toolbar` の修正:**
    *   `NavigationView` と `.toolbar` 修飾子を継続して使用した。
    *   `.toolbar` 内で、`ToolbarItem(placement: .navigationBarLeading)` を使って戻るボタンを配置した。
    *   `ToolbarItem(placement: .principal)` を使って中央揃えのタイトルを配置した。
    *   `.navigationBarTitleDisplayMode(.inline)` と `.navigationBarBackButtonHidden(true)` を設定した。
3.  **責務の分離:**
    *   `@Environment(\.presentationMode)` を削除し、親Viewから `onBack` と `onSuccess` クロージャを受け取るようにした。
4.  **ViewModelとの連携:**
    *   ボタンのアクションで `Task { await viewModel.registerUser(...) }` を呼び出すようにした。
    *   `viewModel.viewState` の変更を `.onChange` で監視し、`.success` の場合に `onSuccess()` を呼び出すようにした。
    *   `viewModel.viewState` が `.error` の場合に `.alert` を表示するようにした。
    *   `viewModel.viewState` が `.loading` の場合にボタンを無効化し、ボタン内に `ProgressView` を表示するようにした。
    *   `ViewState` enum を `Equatable` に準拠させ、`alert(item:)` のエラーを解消した。
    *   `StartView_Previews` を `viewState` に応じて表示を切り替えるように修正した。
    *   `TextField` のプレースホルダーの視認性を向上させるため、`ZStack` を使って黒い `Text` を重ねるように修正した。
→ **完了済み**
