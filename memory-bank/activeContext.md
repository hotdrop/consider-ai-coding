# HomeView.swift UI改善タスク

## 必ず守ること
現在の作業フェーズのタスク完了後、必ずユーザーの確認を待ちます。ユーザーの明示的な指示がない限り、次の親タスク（例: ポイント獲得・利用ボタンのスタイル修正）には絶対に移行しません。

- 現在の作業フェーズ: ポイント獲得・利用ボタンのスタイル修正

## タスク
- [x] `homeCardView()`のレイアウト修正
    - [x] 左上に現在時刻を表示する
    - [x] 中央上に保有ポイント数を表示する
    - [x] 左下にnickNameとemailを表示する
- [x] ポイント獲得・利用ボタンのスタイル修正
    - [x] ポイント獲得ボタンのアイコンを`account_balance_wallet`にし、TextButtonスタイルにする
    - [x] ポイント利用ボタンのアイコンを`shopping_cart`にし、TextButtonスタイルにする
- [x] `historySectionView()`のロジック修正と`HistoryRow` Viewの新規作成および適用
    - [x] 履歴が空の場合は何も表示しない
    - [x] `HistoryRow` structを定義する (dateTime, detail, point)
    - [x] `historySectionView()`で履歴がある場合に`HistoryRow`をリスト表示する
