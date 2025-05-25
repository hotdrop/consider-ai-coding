# Next Task
このファイルは次の実行タスクで追加してほしい機能や修正内容を詳細に記載するドキュメントです。ユーザーに更新権限があり、あなたは更新しないでください。

# やりたいこと
ポイント獲得機能を実装してください。既存の画面はAndroidViewで画面を実装していますが、ポイント獲得機能は`Jetpack Compose`でUIを実装してください。
なお、本プロジェクトはKMPを採用しているため、UseCaseは`shared/src`に実装しているものを使用してください。あなたが実装するレイヤーはUIレイヤー(View, ViewModel)のみです。

## 画面フロー
1. ホーム画面
2. ポイント入力画面
3. ポイント確認画面

## ホーム画面
`ui/home/HomeActivity.kt`は実装済みです。`HomeActivity`の`initView`関数に実装している`binding.pointGetButton.setOnClickListener()`の中身を実装してください。
なお、ポイント獲得が完了してホーム画面に戻ってきた際、`HomeActivity`の`onRefreshData`関数を実行したいのでonActivityResultのように完了したらその情報を戻ってきた際に検知する仕組みも入れてください

## ポイント入力画面
この画面で表示するLabelは`androidApp/src/main/res/values/strings.xml`の`point_get_input_`で始まる文字列定義を利用してください。全て画面上に表示してください。
最大で保持できるポイントは`Point`クラスの`maxAvailablePoint`を使ってください。獲得するポイントの入力フィールドは数値のみで入力可能な桁数を制限してください。桁数は`androidApp/src/main/res/values/integers.xml`の`max_point`となります。
有効なポイント数は0以上、最大で保持できるポイント数未満です。有効なポイント数が入力されたら「確認画面へ進む」ボタンを活性にします。それ以外は非活性です。
入力したポイント数が、最大で保持できるポイント数を超えていた場合は入力フィールドの下に`point_get_input_max_over_error`の文字列を赤フォントで表示します。このエラーメッセージは有効なポイント数が入力されていたら非表示にします。
「確認画面へ進む」ボタンを押すとポイント確認画面に進みます。

## ポイント確認画面
この画面で表示するLabelは`androidApp/src/main/res/values/strings.xml`の`point_get_confirm_`で始まる文字列定義を利用してください。ダイアログメッセージ以外は全て画面上に表示してください。
`point_get_confirm_point_label`の下に大きなフォントサイズで「前画面であるポイント入力画面で入力した有効な獲得ポイント数」を`@color/primary`で表示してください。
「ポイントを獲得する」ボタンを押すと`PointUseCase`の`acquire`関数を実行し、正常終了したらポイントを獲得しました(`point_get_confirm_complete_dialog_message`)メッセージをダイアログでポップアップ表示しホーム画面に戻ります。
ホーム画面では`HomeActivity`の`onRefreshData`関数を実行します。

