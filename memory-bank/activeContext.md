# HomeView.swift UI改善タスク

**現在の作業フェーズ: `homeCardView()`のレイアウト修正**
**この親タスク（`homeCardView()`のレイアウト修正）が完了するまで、次の親タスクには進みません。**
**各親タスクの完了後には、必ずユーザーの確認を待ちます。ユーザーの明示的な指示がない限り、次の親タスク（例: ポイント獲得・利用ボタンのスタイル修正）には絶対に移行しません。**

- [ ] `homeCardView()`のレイアウト修正
    - [ ] 左上に現在時刻を表示する
    - [ ] 中央上に保有ポイント数を表示する
    - [ ] 左下にnickNameとemailを表示する
- [ ] ポイント獲得・利用ボタンのスタイル修正
    - [ ] ポイント獲得ボタンのアイコンを`account_balance_wallet`にし、TextButtonスタイルにする
    - [ ] ポイント利用ボタンのアイコンを`shopping_cart`にし、TextButtonスタイルにする
- [ ] `historySectionView()`のロジック修正
    - [ ] 履歴が空の場合は何も表示しない
- [ ] `HistoryRow` Viewの新規作成と適用
    - [ ] `HistoryRow` structを定義する (dateTime, detail, point)
    - [ ] `historySectionView()`で履歴がある場合に`HistoryRow`をリスト表示する
