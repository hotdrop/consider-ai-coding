# Next Task
このファイルは次の実行タスクで追加してほしい機能や修正内容を詳細に記載するドキュメントです。ユーザーに更新権限があり、あなたは更新しないでください。

# やりたいこと
iOS側の以下のswiftファイルを更新し、ポイント獲得機能を実装したいです。
- `iosApp/iosApp/Sources/ui/pointget/PointGetInputView.swift`
- `iosApp/iosApp/Sources/ui/pointget/PointGetConfirmView.swift`
- `iosApp/iosApp/Sources/ui/pointget/PointGetViewModel.swift`

## ポイント獲得仕様
- ポイント獲得機能はAndroid側で実装済みです。実装コードは以下の通りです。それぞれAndroidの実装を確認し、iOSをSwiftUIで実装してください。
  - `androidApp/src/main/java/jp/hotdrop/considercline/android/ui/pointget/PointGetActivity.kt`
  - `androidApp/src/main/java/jp/hotdrop/considercline/android/ui/pointget/PointGetConfirmScreen.kt`
  - `androidApp/src/main/java/jp/hotdrop/considercline/android/ui/pointget/PointGetInputScreen.kt`
  - `androidApp/src/main/java/jp/hotdrop/considercline/android/ui/pointget/PointGetNavigationHost.kt`
  - `androidApp/src/main/java/jp/hotdrop/considercline/android/ui/pointget/PointGetViewModel.kt`

##　厳守事項
- コード中で利用する文字列は全てLocalizableで定義してください。ただし、LocalizableをXCode以外で追加すると壊れてしまうため、追加したい文字列は実装計画段階で全て洗い出し、ユーザーに追加依頼をしてください。
- Swiftコードは必ず既存の`homeView.swift`や`HomeViewModel.swift`を参考に、同様の設計方針としてください。