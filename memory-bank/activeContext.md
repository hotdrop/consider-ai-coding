# ポイント機能改善（Android）実装計画

## 1. 背景
ポイント獲得機能（`pointget/`）は設計が古く、ポイント利用機能（`pointuse/`）は未実装の部分が残っている。
この計画では、両機能のコード品質とUI/UXを向上させ、一貫性のある高いレベルで完成させることを目的とする。

## 2. 全体方針
- 設計の統一: 両機能において、`ViewModel`の状態管理を`UiState`データクラスに集約し、Composableを`Screen`（状態管理）と`Content`（UI描画）に分割するアーキテクチャを採用する。
- 機能の実装完了: 未実装の機能をすべて実装し、両機能が完全に動作するようにする。
- UI/UXの統一: 両機能のUIデザインを統一し、一貫性のあるユーザー体験を提供する。
- Previewの拡充: UIが取りうる各状態（Loading, Loaded, Error, Success）のPreviewを整備する。

## 3. 詳細計画

### 3.1. `pointget/PointGetViewModel.kt`のリファクタリング
`PointGetViewModel`の改善として、`pointget`機能に必要な状態をすべて含む`PointGetUiState`データクラスを新設し、`ViewModel`内の状態管理を`MutableStateFlow<PointGetUiState>`に一本化する。
- `PointGetUiState`の定義: 
  - pointget機能に必要な状態をすべて含む`PointGetUiState`というデータクラスを新たに作成します。これには、画面のローディング状態、エラーメッセージ、現在のポイント、入力ポイント、入力エラーの有無、ボタンの有効状態、そしてポイント獲得処理の状態（待機、処理中、成功、失敗）などを含めます。
- ViewModelの修正: 
  - PointGetViewModel内にある複数のMutableStateFlowやChannelを廃止します。
  - 代わりに、MutableStateFlow<PointGetUiState>を一つだけ保持するように変更します。
  - init、inputPoint、acquirePointなどの関数は、このUiStateを更新するロジ-ックに書き換えます。

### 3.2. pointget/PointGetInputScreen.kt のリファクタリング
- `PointGetInputScreen`の責務分離: 
  - 現在の`PointGetInputScreen`はUIの定義と状態の監視が混在しています。これを`Screen`と`Content`に分離します。
  - リファクタリング後の`PointGetInputScreen`は、ViewModelからUiStateを監視し、その状態（例：isLoading, errorMessageの有無）に応じて、LoadingView、ErrorView、または後述のPointGetInputContentといった適切なComposableを呼び出す役割に限定します。
- `PointGetInputContent`の作成: 
  - `PointUseInputContent`を参考に、UIの具体的なレイアウトを担当す
  - `PointGetInputContent`という新しいComposable関数を作成します。
  - この関数は、UiStateを引数として受け取り、ポイント残高の表示、入力フィールド、確認ボタンなどの既存のUI要素を配置します。
  - レイアウトはすでに完成されています。コンポーネントを移動するだけで絶対にコンポーネントの並び順、スタイル、Modifier等は修正しません。
  - ユーザーからの入力イベント（テキストフィールドの変更、ボタンクリックなど）は、コールバック関数を通じてPointGetInputScreenに通知され、ViewModelに渡されます。この処理もすでに実装済みですので挙動を勝手に変えません。

### 3.3. PointGetInputScreenのPreview拡充
pointuse機能と同様に、`PointGetInputScreen`が取りうる各UI状態に対応したPreviewを作成します。これにより、UIの確認が容易になり、開発効率が向上します。
- `@Preview`関数の追加:
  - Loading状態: PointGetInputContentが表示される前の、データ読み込み中の状態を示すPreview。
  - Loaded状態: 正常にデータが読み込まれ、ユーザーがポイントを入力できる状態のPreview。PointGetInputContentにダミーのUiStateを渡して表示します。
  - Error状態: データ読み込みに失敗し、エラーメッセージが表示される状態のPreview。

これらのPreviewは、ViewModelに依存せず、PointGetInputContentやErrorViewなどのComposableに直接ダミーデータを渡すことで実現します。

### 3.4 pointget/PointGetConfirmScreen.kt のリファクタリング
`PointGetInputScreen`と同様に`PointGetConfirmScreen`も責務を分離し、UIとロジックの結合を弱めます。

- `PointGetConfirmScreen`の責務分離: 
  - 現在の`PointGetConfirmScreen`は、UIの表示、状態の保持(`mutableStateOf`)、`LaunchedEffect`によるイベント監視など、多くの責務を担っています。
  - リファクタリング後の`PointGetConfirmScreen`は、ViewModelからUiStateを監視し、その状態(`isLoading`, `isSuccess`, `errorMessage`など)に応じて、`LoadingView`, `SuccessDialog`, `ErrorDialog`, または後述の`PointGetConfirmContent`を呼び出す役割に限定します。
- `PointGetConfirmContent`の作成: 
  - `PointUseConfirmContent`を参考に、確認画面の具体的なUIレイアウトを担当する`PointGetConfirmContent`という新しいComposable関数を作成します。
  - レイアウトはすでに完成されています。コンポーネントを移動するだけで絶対にコンポーネントの並び順、スタイル、Modifier等は修正しません。
  - ボタンのクリックイベントはコールバックを通じてViewModelに通知されます。この処理もすでに実装済みですので挙動を勝手に変えません。

### 3.5. PointGetConfirmScreenのPreview拡充
`PointUseConfirmScreen`と同様に、確認画面が取りうる各UI状態に対応したPreviewを作成します。
- `@Preview`関数の追加:
  - Content表示状態: `PointGetConfirmContent`が正常に表示されている状態のPreview。
  - Loading状態: ポイント獲得処理中に表示される`CircularProgressIndicator`のPreview。
  - Success状態: ポイント獲得成功時に表示される`SuccessDialog`のPreview。
  - Error状態: エラー発生時に表示される`ErrorDialog`のPreview。

### 3.6. `pointuse/PointUseConfirmScreen.kt` の実装完了とUI統一
現在の`PointUseConfirmScreen`では、成功ダイアログを閉じた後の処理が不完全です。UIデザインを`PointUseConfirmScreen`にあわせるとともに、未実装の機能を完成させます。

- 必ず守る厳守すべきこと: 
  - `PointUseViewModel.kt`はすでに完成されていて修正の余地はありません。絶対に`PointUseViewModel.kt`は修正を加えません。
- `PointUseConfirmScreen.kt`の実装完了:
  - `ViewModel`から提供される`UiState`（`isLoading`, `isSuccess`, `errorMessage`）を監視し、状態に応じた適切なComposable（`LoadingView`, `SuccessDialog`, `ErrorDialog`など）を表示するロジックを実装します。
  - isSuccessがtrueになったらSuccessDialogを表示します。ダイアログが閉じられた際、ViewModelのUiStateをリセットすると同時に、onCompleteコールバックを呼び出して、ポイント獲得フローが完了したことを呼び出し元（Activity）に通知します。これにより、画面遷移などの後続処理が可能になります。
- UIデザインの統一:
  - `PointUseConfirmContent`内のComposable（TextやButtonなど）のスタイル（フォントサイズ、色、パディング、ボタン幅など）を、`PointGetConfirmContent`のスタイルと一致させます。これにより、アプリ全体で一貫性のあるデザインを提供します
