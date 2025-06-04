# Active Context

## 必ず守ること(編集禁止)
現在のタスクに書かれた内容は必ず1つずつ「Plan/Act」を経てユーザーに確認しながら実装すること。Planで実装計画を立てた後、Actで実装に入った場合「現在のタスク」に列挙されたタスクを一度に全部こなしてはいけません。必ず1つずつPlan→Actを行い、1つ完了したらユーザーに確認し、Planで再び実装計画から行ってください。

## 現在のタスク
iOSアプリの初期画面（アプリ起動画面、アプリ開始画面）をSwiftUIで実装する。

### やりたいこと
1.  **リソースファイルの移植:**
    *   画像ファイル (`home_card.png`, `start.png`) をiOSプロジェクトの `Assets.xcassets` に追加する。
    *   `./commonResource/` 配下のSVGアイコンファイル2点をiOSプロジェクトの `Assets.xcassets` にベクター画像として追加する。
    *   Androidの `colors.xml` の内容をiOSプロジェクトの `Assets.xcassets` にColor Setとして定義する。
    *   Androidの `strings.xml` の内容をiOSプロジェクトの `Localizable.strings` に定義する。
2.  **アプリ起動画面の実装:**
    *   `iosApp/iosApp/Sources/Views/App/MainView.swift` を作成し、SwiftUIでUIを構築する。
    *   `iosApp/iosApp/Sources/ViewModels/App/MainViewModel.swift` を作成し、KMPのUseCaseを利用したロジックを実装する。
3.  **アプリ開始画面の実装:**
    *   `iosApp/iosApp/Sources/Views/App/StartView.swift` を作成し、SwiftUIでUIを構築する。
    *   `iosApp/iosApp/Sources/ViewModels/App/StartViewModel.swift` を作成し、KMPのUseCaseを利用したロジックを実装する。
4.  **`activeContext.md` の更新:**
    *   上記タスクの進捗と完了状況を`activeContext.md`に反映する。

### 実装計画
1.  **リソースファイルの移植:**
    *   **画像ファイル:**
        *   `androidApp/src/main/res/drawable/home_card.png` を `iosApp/iosApp/Assets.xcassets` に登録します。
        *   `androidApp/src/main/res/drawable/start.png` を `iosApp/iosApp/Assets.xcassets` に登録します。
    *   **SVGファイル (アイコン):**
        *   `./commonResource/` に配置された `account_balance_wallet_24.svg` (仮名) を `iosApp/iosApp/Assets.xcassets` にベクター画像として登録します。
        *   `./commonResource/` に配置された `shopping_cart_24.svg` (仮名) を `iosApp/iosApp/Assets.xcassets` にベクター画像として登録します。
    *   **カラー定義:**
        *   `androidApp/src/main/res/values/colors.xml` の内容を `iosApp/iosApp/Assets.xcassets` にColor Setとして定義します。
    *   **文字列定義:**
        *   `androidApp/src/main/res/values/strings.xml` の内容を `iosApp/iosApp/Resources/Localization/en.lproj/Localizable.strings` (英語をデフォルトとし、必要に応じて日本語リソースも作成) に定義します。
2.  **アプリ起動画面の実装 (`MainView.swift`, `MainViewModel.swift`):**
    *   `iosApp/iosApp/Sources/Views/App/MainView.swift` を作成します。
    *   `iosApp/iosApp/Sources/ViewModels/App/MainViewModel.swift` を作成します。
    *   Androidの `MainViewModel.kt` および `activity_main.xml` を参考に、SwiftUIで画面とロジックを実装します。
3.  **アプリ開始画面の実装 (`StartView.swift`, `StartViewModel.swift`):**
    *   `iosApp/iosApp/Sources/Views/App/StartView.swift` を作成します。
    *   `iosApp/iosApp/Sources/ViewModels/App/StartViewModel.swift` を作成します。
    *   Androidの `StartViewModel.kt` および `activity_start.xml` を参考に、SwiftUIで画面とロジックを実装します。
4.  **`activeContext.md`の更新:**
    *   上記タスクの進捗と完了状況を`activeContext.md`に反映する。
