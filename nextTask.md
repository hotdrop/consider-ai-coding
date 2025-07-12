# Next Task
このファイルは次の実行タスクで追加してほしい機能や修正内容を詳細に記載するドキュメントです。ユーザーに更新権限があり、あなたは更新しないでください。

# やりたいこと
`androidApp/`の`pointget/`ディレクトリ(ポイント獲得機能)と`pointuse/`ディレクトリ(ポイント利用機能)のコード品質を改善して未実装の機能を実装したいです。以下の方針に従って実装計画を立てて`memory-bank/activeContext.md`に出力してください。

# 方針1: 現在完璧に動作するのはポイント獲得機能である。UIもポイント獲得機能にあわせる
ポイント獲得機能`pointget/`は設計が古いものの実機動作テストで合格しています。対してポイント利用機能`pointuse/`は動作確認テストに不合格です。
主に`PointGetConfirmScreen.kt`が完成しておらず動かない機能があったり、ポイント利用機能に比べてボタンの幅指定がおかしかったりTextのフォントカラーがおかしいためです。`PointUseConfirmScreen.kt`のUIデザインにあわせて`PointGetConfirmScreen.kt`のUIを修正するとともに完了ダイアログや`onComplete`を呼んで機能を完成させてください

# 方針2: ポイント獲得機能の設計が良くないのでポイント利用機能の設計を採用したい
以下、改善したい点です。

## Screenコンポーザブルの関数分割をポイント利用機能にあわせる
例えば`PointGetInputScreen.kt`ですが`PointGetInputScreen`関数にUIコンポーネントを直接置いています。これだとPreview関数でViewModel扱わなければならず良くないです。`PointUseInputScreen.kt`の`PointUseInputContent`のようにコンテンツは分けるべきで、Screen関数の直下はuiStateの状態管理やEffectの扱いに閉じるべきです。
これは`PointGetConfirmScreen.kt`も同様です。

## 状態管理の方法をポイント利用機能にあわせる
`PointUseInputScreen.kt`と`PointGetConfirmScreen.kt`は`mutableStateOf`を多用していますがよくないです。ポイント利用機能のように`uiState`でまとめるべきです。
あと`PointUseInputScreen.kt`は最初のLoadingもなくいきなり画面表示しているのでここもポイント利用機能にあわせてほしいです。

# 方針3: ポイント獲得機能のPreviewを状態の数だけ設ける
上記方針で修正すれば、ポイント獲得機能`pointget/`もポイント利用機能`pointuse/`と同様、Loading,Loaded,Error,Successのような状態を設けると思うので、その分のPreview関数を作ってください。