# Active Context

## 現在の主要タスク: ポイント獲得機能の実装

`nextTask.md` の指示に基づき、ポイント獲得機能を実装します。主な実装内容は以下の通りです。

### 1. `shared/src/commonMain/kotlin/jp/hotdrop/considercline/model/Point.kt` の修正 (完了)
*   `companion object` 内の `private const val MAX_POINT = 1000` を削除しました。
*   既存の `maxAvailablePoint` プロパティを削除しました。
*   `getMaxAvailablePoint(maxPoint: Int)` 関数を新たに追加しました。

### 2. ホーム画面 (`HomeActivity.kt`) の修正 (完了)
*   `androidApp/src/main/java/jp/hotdrop/considercline/android/ui/home/HomeActivity.kt`
*   `initView` 関数の `binding.pointGetButton.setOnClickListener()` 内に、ポイント入力画面 (Compose Activity) への遷移処理を実装しました。
*   Activity Result API ( `registerForActivityResult` ) を利用した画面遷移と結果のコールバック処理を実装し、ポイント獲得完了後に `HomeActivity` の `onRefreshData()` を呼び出すようにしました。

### 3. ポイント入力画面 (新規 Jetpack Compose Activity)
*   **ファイル作成:**
    *   `androidApp/src/main/java/jp/hotdrop/considercline/android/ui/pointget/PointGetInputActivity.kt` (Activity)
    *   `androidApp/src/main/java/jp/hotdrop/considercline/android/ui/pointget/PointGetInputViewModel.kt` (ViewModel)
    *   `androidApp/src/main/java/jp/hotdrop/considercline/android/ui/pointget/PointGetInputScreen.kt` (Composable関数)
*   **ViewModel (`PointGetInputViewModel.kt`):**
    *   `KmpUseCaseFactory.pointUseCase` を利用して現在のポイント残高 (`balance`) を取得します。
    *   `androidApp/src/main/res/values/integers.xml` から `max_point` の値 (5000) を取得します。
    *   `point.getMaxAvailablePoint(max_pointの値)` を呼び出して、保持可能な最大ポイントを計算します。
    *   入力されたポイント数を保持する `StateFlow<String>` を持ちます。
    *   入力ポイントのバリデーションロジックを実装します (0以上、かつ保持可能な最大ポイント未満)。
    *   エラーメッセージ (`point_get_input_max_over_error`) の表示状態を管理する `StateFlow<Boolean>` を持ちます。
    *   「確認画面へ進む」ボタンの活性状態を管理する `StateFlow<Boolean>` を持ちます。
*   **Screen (`PointGetInputScreen.kt`):**
    *   `strings.xml` の `point_get_input_` で始まるラベルを表示します。
    *   ポイント入力用の `TextField` を配置 (数値のみ、4桁制限)。
    *   エラーメッセージを赤色フォントで表示。
    *   「確認画面へ進む」ボタンを配置し、活性/非活性を制御。クリックでポイント確認画面へ遷移。
*   **Activity (`PointGetInputActivity.kt`):**
    *   ViewModelをインジェクト (Hilt) し、`PointGetInputScreen` を呼び出します。

### 4. ポイント確認画面 (新規 Jetpack Compose Activity)
*   **ファイル作成:**
    *   `androidApp/src/main/java/jp/hotdrop/considercline/android/ui/pointget/PointGetConfirmActivity.kt` (Activity)
    *   `androidApp/src/main/java/jp/hotdrop/considercline/android/ui/pointget/PointGetConfirmViewModel.kt` (ViewModel)
    *   `androidApp/src/main/java/jp/hotdrop/considercline/android/ui/pointget/PointGetConfirmScreen.kt` (Composable関数)
*   **ViewModel (`PointGetConfirmViewModel.kt`):**
    *   入力された獲得ポイント数を保持します。
    *   `KmpUseCaseFactory.pointUseCase.acquire()` を呼び出す関数を実装 (`viewModelScope` で実行)。
    *   成功/失敗をUIに通知するための `StateFlow` を持ちます。
*   **Screen (`PointGetConfirmScreen.kt`):**
    *   `strings.xml` の `point_get_confirm_` で始まるラベルを表示します。
    *   獲得ポイント数を大きく表示。
    *   「ポイントを獲得する」ボタンを配置。
    *   成功時: `point_get_confirm_complete_dialog_message` をダイアログ表示し、ホーム画面に戻り `HomeActivity.onRefreshData()` を実行。
*   **Activity (`PointGetConfirmActivity.kt`):**
    *   ViewModelをインジェクト (Hilt) し、`PointGetConfirmScreen` を呼び出します。

*この`activeContext.md`は、プロジェクトの現在の焦点や主要な活動を記録します。Memory Bankの他のドキュメントと合わせて参照してください。*
