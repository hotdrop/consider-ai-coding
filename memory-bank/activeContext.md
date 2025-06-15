# Active Context

## 必ず守ること(編集禁止)
現在のタスクに書かれた内容は必ず1つずつ「Plan/Act」を経てユーザーに確認しながら実装すること。Planで実装計画を立てた後、Actで実装に入った場合「現在のタスク」に列挙されたタスクを一度に全部こなしてはいけません。必ず1つずつPlan→Actを行い、1つ完了したらユーザーに確認し、Planで再び実装計画から行ってください。

## 現在のタスク
iOSアプリの初期画面（アプリ起動画面、アプリ開始画面）をSwiftUIで実装する。

### やりたいこと
1.  **カラー定義の移植:**
    *   Androidの `colors.xml` の内容をiOSプロジェクトの `Assets.xcassets` にColor Setとして定義する。
2.  **文字列定義の移植:**
    *   Androidの `strings.xml` の内容をiOSプロジェクトの `Localizable.strings` に定義する。
3.  **アプリ起動画面の実装:**
    *   `iosApp/iosApp/Sources/Views/App/MainView.swift` を作成し、SwiftUIでUIを構築する。
    *   `iosApp/iosApp/Sources/ViewModels/App/MainViewModel.swift` を作成し、KMPのUseCaseを利用したロジックを実装する。
4.  **アプリ開始画面の実装:**
    *   `iosApp/iosApp/Sources/Views/App/StartView.swift` を作成し、SwiftUIでUIを構築する。
    *   `iosApp/iosApp/Sources/ViewModels/App/StartViewModel.swift` を作成し、KMPのUseCaseを利用したロジックを実装する。
5.  **`activeContext.md` の更新:**
    *   上記タスクの進捗と完了状況を`activeContext.md`に反映する。
    *   画像ファイルとSVGアイコンファイルの設定はユーザーが行う。

### 実装計画
ユーザーが画像ファイルとSVGアイコンファイルをXcodeプロジェクトに追加するのを待つ。
その後、以下のタスクを順に進める。

1.  [x] **カラー定義の移植:** (完了)
    *   `androidApp/src/main/res/values/colors.xml` の内容を `iosApp/iosApp/Assets.xcassets` にColor Setとして定義します。
2.  [x] **文字列定義の移植:** (完了)
    *   `androidApp/src/main/res/values/strings.xml` の内容を `iosApp/iosApp/Resources/Localization/en.lproj/Localizable.strings` (英語をデフォルトとし、必要に応じて日本語リソースも作成) に定義します。
3.  **アプリ起動画面の実装 (`MainView.swift`, `MainViewModel.swift`):**
    *   `iosApp/iosApp/Sources/Views/App/MainView.swift` を作成します。
    *   `iosApp/iosApp/Sources/ViewModels/App/MainViewModel.swift` を作成します。
    *   Androidの `MainViewModel.kt` および `activity_main.xml` を参考に、SwiftUIで画面とロジックを実装します。
4.  **アプリ開始画面の実装 (`StartView.swift`, `StartViewModel.swift`):**
    *   `iosApp/iosApp/Sources/Views/App/StartView.swift` を作成します。
    *   `iosApp/iosApp/Sources/ViewModels/App/StartViewModel.swift` を作成します。
    *   Androidの `StartViewModel.kt` および `activity_start.xml` を参考に、SwiftUIで画面とロジックを実装します。
5.  **`activeContext.md`の更新:**
    *   上記タスクの進捗と完了状況を`activeContext.md`に反映する。
