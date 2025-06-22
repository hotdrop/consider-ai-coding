# Next Task
このファイルは次の実行タスクで追加してほしい機能や修正内容を詳細に記載するドキュメントです。ユーザーに更新権限があり、あなたは更新しないでください。

# やりたいこと
`StartView.swift`と`StartViewModel.swift`のUIがandroidと異なっているため、以下の修正を行なってほしいです。

- `StartView.swift`のUIが`./taskImages/ios.png`のようにandroidと違っているので`./taskImages/android.png`と同様のUIにしてください。
- `StartView.swift`と`StartViewModel.swift`のロジックがSwiftUIのルールに沿っておらず可読性や保守性が低いコードになっているためルールに沿ってリファクタリングしてほしいです。
- HomeViewは未実装なので遷移ロジックはTODOのままで良いです。