# Active Context

## 必ず守ること(編集禁止)
現在のタスクに書かれた内容は必ず1つずつ「Plan/Act」を経てユーザーに確認しながら実装すること。Planで実装計画を立てた後、Actで実装に入った場合「現在のタスク」に列挙されたタスクを一度に全部こなしてはいけません。必ず1つずつPlan→Actを行い、1つ完了したらユーザーに確認し、Planで再び実装計画から行ってください。

## 現在の主要タスク: ポイント獲得機能の実装

*この`activeContext.md`は、プロジェクトの現在の焦点や主要な活動を記録します。Memory Bankの他のドキュメントと合わせて参照してください。*

### 実装計画

1. [x] ポイント確認画面のUI実装 (`PointGetConfirmScreen.kt`) - ✅完了
    *   Jetpack Composeでポイント確認画面のUIを実装します。
    *   表示する文字列は `strings.xml` の `point_get_confirm_` プレフィックスを持つものを利用します。
    *   獲得ポイント数は大きなフォントサイズ、色は `AppColor.PrimaryColor` で表示します。このポイント数は `PointGetViewModel` の `inputPoint` (`StateFlow<String>`) から取得します。
    *   「ポイントを獲得する」ボタンを配置します。
    *   `@Preview` を実装し、UIの状態を確認できるようにします。

2. [x] 画面遷移の実装 (`PointGetNavigationHost.kt`, `PointGetViewModel.kt`) - ✅完了
    *   `PointGetInputScreen.kt` から `PointGetConfirmScreen.kt` へ遷移できるように、`PointGetNavigationHost.kt` に新しいルートを追加します。
    *   既存のTODOコメントとコメントアウトされたコードは全て無視してください。

3. [x] ポイント獲得処理の実装 (`PointGetViewModel.kt`, `PointGetConfirmScreen.kt`) - ✅完了
    *   `PointGetConfirmScreen.kt` の「ポイントを獲得する」ボタンが押された際に、`PointGetViewModel.kt` の関数を呼び出します。
    *   `PointGetViewModel.kt` に、`PointUseCase` の `acquire` 関数を呼び出す処理を実装します。
        *   `KmpUseCaseFactory` を経由して `PointUseCase` のインスタンスを取得します。
        *   `acquire` 関数の実行結果に応じて、UIの状態（ローディング、成功、エラー）を更新する処理を実装します。

4. [ ] ポイント獲得完了ダイアログ表示と画面遷移 (`PointGetConfirmScreen.kt`, `PointGetViewModel.kt`, `PointGetActivity.kt`)
    *   ポイント獲得処理が正常に完了した場合、`PointGetConfirmScreen.kt` で `strings.xml` の `point_get_confirm_complete_dialog_message` をメッセージとするダイアログを表示します。
    *   ダイアログが閉じられたら、`PointGetActivity.kt` に処理結果を通知します。
        *   `PointGetViewModel` に、ポイント獲得処理の成功を通知するための `SharedFlow` または同様の仕組みを追加します。
        *   `PointGetConfirmScreen.kt`はこの`Flow` を監視し、成功通知を受け取ったら引数の`onComplete`を呼びます。
        *   `onComplete`は`PointGetNavigationHost.kt`から渡されますが、その実装は`PointGetActivity` で行います。ActivityでonCompleteが呼ばれたら  `setResult(RESULT_OK)` を実行して `finish()` します。
        *   `HomeActivity` 側では、`ActivityResultLauncher` で `RESULT_OK` を受け取った場合に `onRefreshData` を実行する既存の仕組みが動作します。
