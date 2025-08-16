# iOS PointGet 実装計画（Android 実装を準拠）

## 目的と範囲
- iOS の PointGet フロー（入力 → 確認 → 完了）を SwiftUI で実装し、Android と同等のUX/バリデーション/非同期処理を再現する。
- 既存の `shared`（KMM）`PointUseCase` を利用してポイント取得・獲得を行う。

## 参照（Android 実装）
- VM: `androidApp/.../pointget/PointGetViewModel.kt`
- 画面: `PointGetInputScreen.kt`, `PointGetConfirmScreen.kt`
- ルーティング: `PointGetNavigationHost.kt`

## 現状の iOS 実装（確認）
- ルーティング: `PointGetView`（`NavigationStack` + `PointGetRouter`）
- VM: `PointGetViewModel.swift`（`load`, `inputPoint`, `acquirePoint`, `validateInput` 実装済）
- 画面: `PointGetInputView.swift`（入力画面はほぼ実装済、Confirm 遷移は TODO）
- 画面: `PointGetConfirmView.swift`（未実装）

## 実装タスク
1) ルーティング接続
   - `PointGetRouter.input(...)` の `onNavigateToConfirm` で `path.append(.confirm)` を呼ぶ。
   - Confirm → 完了: `acquirePoint` 成功で `PointGetView` の `onChange(acquireEventState)` が `.complete` に遷移済（維持）。
2) Confirm 画面の実装（`iosApp/.../screen/PointGetConfirmView.swift`）
   - 引数: `viewModel: PointGetViewModel` を受け取り、`viewModel.viewState` から `inputPoint` を参照。
   - UI: 概要文、入力ポイント表示、実行ボタン、処理中インジケータ、エラーダイアログ（Alert）。
   - 実行: ボタンで `Task { await viewModel.acquirePoint(inputPoint: inputPoint) }`。
   - 成功: 画面内では何もしない（遷移は `PointGetView` の `onChange` が担当）。
   - 失敗: `acquireEventState == .error` を監視して `Alert` 表示、閉じると `acquireEventState = nil` に戻す。
3) 入力画面からの遷移
   - `PointGetInputView` の `onNavigateToConfirm` コールバックで画面遷移を発火（Router 経由）。
   - 併せて `PointGetViewModel` の `viewState` に保持されている `inputPoint` をそのまま Confirm で表示。
4) バリデーション/上限
   - 現在 `maxPoint=20000` を `PointGetViewModel` に保持。将来は設定/定数化（`Localizable` or Config）を TODO コメントで明示。
5) 文言/多言語
   - 既存キー（例: `point_get_input_*`）に合わせて Confirm 用キーを `Localizable.xcstrings` に追加（例: `point_get_confirm_overview`, `..._execute_button`, `..._error`）。
6) アクセシビリティ/デザイン
   - ボタン活性/非活性、カラー（`Color("themeColor")` 等）を入力画面と揃える。

## 画面と状態の要点
- ViewState
  - `.loading` → 初期取得（`load()`）
  - `.success(currentPoint, inputPoint, errorMessage, isEnableConfirm)`
  - `.error(message)`
- Acquire イベント
  - `.success` → `PointGetView` で履歴クリアし `.complete` へ
  - `.error(message)` → Confirm 画面で Alert 表示

## テスト計画（手動）
- 初期起動で `load()` 成功し Input に遷移する。
- 0 以下/上限超過でエラー文言表示、Confirm ボタン非活性。
- 正常値で Confirm 遷移、実行中スピナー表示、成功で Complete へ遷移。
- 失敗時にエラー Alert が表示され閉じると再操作可能。

## 完了の定義（DoD）
- 入力→確認→完了の一連の遷移が再現できる。
- Android と同等のバリデーション/メッセージ/ローディングが実装。
- 文言が `Localizable.xcstrings` に登録され、日本語/英語で崩れない。
- `shared` の `PointUseCase` を経由して処理できる。
