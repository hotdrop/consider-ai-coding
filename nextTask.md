# Next Task
このファイルは次の実行タスクで追加してほしい機能や修正内容を詳細に記載するドキュメントです。ユーザーに更新権限があり、あなたは更新しないでください。

# やりたいこと
iOSで以下の画面を実装しましたがエラーがいくつか発生しているのと動作確認ができていないので修正をお願いしたいです。

- アプリ起動画面: `iosApp/iosApp/Sources/ui/start`
  - StartView.swift
  - StartViewModel.swift
- アプリ開始画面: `iosApp/iosApp/Sources/ui/`
  - MainView.swift
  - MainViewModel.swift

# 進め方
ClineであるあなたはVSCode上で動作するため、iOSのエラーを検知できません。ユーザーがXCode上でビルドしてエラーを摘出したり動作確認しますので、あなたはそのエラーを順番に解消してください。

# 現時点で把握している問題
- エントリポイントと思われる`iOSApp.swift`が`ContentView()`を呼び出しているが、ここは`MainView.swift`のViewにしたい。
- Androidでは`App.kt`で`KmpUseCaseFactory.init(AndroidPlatformDependencies(this))`を実行している。iOSでも同様の初期化処理が必要ではないか？
- `MainViewModel.swift`の28行目`self.viewState = .loaded(appSetting.userId)`でuserIdが`Value of optional type 'String?' must be unwrapped to a value of type 'String'`エラーになっている
- `StartView.swift`の69行目で`.alert`が`Instance method 'alert(item:content:)' requires that 'String' conform to 'Identifiable'`エラーになっている
- `StartViewModel.swift`の32行目で`try await appSettingUseCase.registerUser(uiState.inputNickName, uiState.inputEmail)`で`Missing argument labels 'nickname:email:' in call`エラーになっている