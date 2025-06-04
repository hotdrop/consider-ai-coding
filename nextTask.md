# Next Task
このファイルは次の実行タスクで追加してほしい機能や修正内容を詳細に記載するドキュメントです。ユーザーに更新権限があり、あなたは更新しないでください。

# やりたいこと
iOSで以下の画面を実装してください。既に`androidApp/`で全く同じ機能を実装しているためAndroidを参考にしてください。

- アプリ起動画面
- アプリ開始画面

## はじめにやってほしいこと
リソースファイルをそろえてほしいです。Androidでいうと`res/`の中に定義しているdrawableの画像ファイルやアイコンファイル、`res/values`のカラー定義、文字列定義をiOSアプリ側に定義してください。
AndroidのvectorファイルはiOS側で使えない可能性もあるので、その際はSVGファイルをお渡しします。

## アプリ起動画面
`androidApp/`のソースコードだと`MainActivity.kt`と`MainViewModel.kt`に相当します。
Androidのこの画面はComposeではなくAndroidViewで実装しているので、画面定義は`res/layout/activity_main.xml`を参照してください。

## アプリ開始画面
`androidApp/`のソースコードだと`StartActivity.kt`と`StartViewModel.kt`に相当します。
Androidのこの画面はComposeではなくAndroidViewで実装しているので、画面定義は`res/layout/activity_start.xml`を参照してください。
