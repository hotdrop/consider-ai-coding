# Next Task
このファイルは次の実行タスクで追加してほしい機能や修正内容を詳細に記載するドキュメントです。ユーザーに更新権限があり、あなたは更新しないでください。

# やりたいこと
`HomeView.swift`のUIがAndroidのUIと多々乖離しているので以下の修正を行ってほしい。

- `homeCardView()`の左上は現在時刻を表示する
- `homeCardView()`の中央上部分に保有ポイント数(home_point_value)を表示する
- `homeCardView()`の左下にnickNameとemailを表示する。nickNameとemailがnilになることはない。
- ポイント獲得ボタンのアイコンは`account_balance_wallet`を使い、ボタンの枠は表示しない。TextButtonと同様とする。
- ポイント利用ボタンのアイコンは`shopping_cart`を使い、ボタンの枠は表示しない。TextButtonと同様とする。
- `historySectionView()`の挙動において、履歴データが空の場合は何も表示しない。
- `historySectionView()`の挙動において、履歴データが1件以上ある場合は`HistoryRow`というstructのViewを別途定義して、データ件数分だけ`HistoryRow`を縦方向にaddしていく。
- `HistoryRow`は左上にdateTime、その下にdetail、右に上下中央寄せでpointを「ポイント」というラベルをつけて表示する。緑や赤色はつけない