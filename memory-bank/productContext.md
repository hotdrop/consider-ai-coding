# Product Context

## プロジェクトの目的
Kotlin MultiPlatform (KMP) を利用し、AndroidとiOSのビジネスロジックやデータレイヤーを共通化する手法を検証することを目的としています。

## 解決する問題
モバイルアプリケーション開発において、AndroidとiOSのプラットフォーム間でビジネスロジックやデータアクセスロジックのコードを個別に開発・保守する必要があり、開発効率の低下や品質のばらつきが生じるという課題があります。
本プロジェクトは、Kotlin MultiPlatform (KMP) を活用することで、これらの共通ロジックをプラットフォーム間で共有し、上記の課題を解決することを目指します。

## 主要技術スタック
-  Kotlin MultiPlatform (KMP): AndroidとiOSでビジネスロジックとデータレイヤーを共通化するための基盤技術。
- AndroidApp
  - Jetpack Compose
  - Dagger Hilt
- iOSApp
  - SwiftUI
- common(KMP共通ロジック)
  - Ktor: API通信を行うためのHTTPクライアントライブラリ。`shared/src/commonMain/` にクライアント実装を配置します。
  - SQLDelight: ローカルデータベースを扱うためのライブラリ。型安全なSQLクエリを生成し、`shared/src/commonMain/` にDBスキーマ (`.sq`ファイル) と生成インターフェースを配置します。
  - kotlinx.coroutines: 非同期処理を実現するための標準ライブラリ。`suspend`関数やFlowを積極的に活用します。

## ディレクトリ構成
```
androidApp/   # Android アプリケーションモジュール。原則UIレイヤーのみ実装する
  src/
  build.gradle.kts
gradle/
  wrapper/
  libs.versions.toml  # 依存ライブラリが記載されたtomlファイル。readは許可するがwriteは許可しない
iosApp/        # iOS アプリケーションモジュール
  iosApp/
  iosApp.xcodeproj/
memory-bank/   # 実装計画を立てる際、この中にあるすべてのファイルを必ず読む
shared/        # 共通ロジック（KMP共通）
  src/
    androidMain/  # Android 固有実装
    commonMain/   # プラットフォームに依存しない共有ロジック (Model, Repository インターフェース, UseCase, Ktor Client, SQLDelight スキーマ/クエリ, `expect` 宣言など)
    iosMain/      # iOS 固有実装
  build.gradle.kts
```

## アーキテクチャ概要
本プロジェクトでは、Kotlin MultiPlatform (KMP) を最大限に活用し、プラットフォーム間で可能な限りコードを共通化することを目指します。主要なアーキテクチャパターンは以下の通りです。

- ネイティブViewレイヤー:
  - Android (`androidApp/`): Jetpack Compose を使用してUIを実装します。既存でAndroidViewを使用していますが、そこには手を加えません。
  - iOS (`iosApp/`): SwiftUI を使用してUIを実装します。
  - 各プラットフォームで MVVM (Model-View-ViewModel) アーキテクチャを採用します。ViewModel は各プラットフォームで実装しますが、ビジネスロジックの実行は `shared` モジュールの UseCase を利用します。
- UseCase レイヤー (KMP共通 - `shared/src/commonMain/`):
  - アプリケーション固有のビジネスロジックを実装します。
  - 1つ以上のRepositoryを組み合わせて特定の機能を提供します。
  - ネイティブのViewModelは、`KmpUseCaseFactory` を経由してUseCaseのインスタンスを取得し、ビジネスロジックやデータ取得を実行します。Repository層はUseCaseによってカプセル化されます。
- データレイヤー (KMP共通 - `shared/src/commonMain/`):
  - Repositoryパターンを採用します。
  - Repositoryは、データソース（Remote/Local）を抽象化し、UseCase に一貫したインターフェースを提供します。

## UI実装方針
- Android: Jetpack Compose を全面的に採用します。AndroidViewベースのUI実装は行いません。
- iOS: SwiftUI を全面的に採用します。UIKitの利用やSwiftのinterop層の実装は行いません。

## Dependency Injection (DI) 方針
- `shared/` (KMP共通ロジック): DIライブラリは使用しません。
  - モデルクラスを除くすべてのクラスは、可能な限りコンストラクタインジェクションを使用して疎結合を保ちます。
  - UseCaseのインスタンスは、必ず`KmpUseCaseFactory`クラスを経由して取得します。これにより、UseCase以下のRepository層などの依存関係はカプセル化されます。
- `androidApp/` (Androidネイティブ実装): `Dagger Hilt`を使用して依存性注入を行います。

## 非同期処理
- `shared/`モジュール内のpublicなI/O処理や時間のかかる処理は、原則として`suspend`関数として実装します。
- コルーチンスコープとディスパッチャの管理には注意が必要です。
  - ViewModel内の処理は、各プラットフォームのViewModelのライフサイクルに紐づくコルーチンスコープ（例: Androidの`viewModelScope`）で実行します。
  - Repository層などでのバックグラウンド処理には、`Dispatchers.IO`や`Dispatchers.Default`などを適切に使用します。

## イミュータビリティ
- 可能な限りイミュータブルなデータクラス（`val`プロパティのみを持つ`data class`）を使用し、状態の変更を予測可能にします。

## ユーザー体験の目標 (UX Goals)
- シンプルで直感的な操作性: ユーザーが迷うことなく、ポイントの獲得、利用、履歴確認といった主要な機能をスムーズに操作できること。
- 明確な情報提示: ポイント残高や履歴情報が分かりやすく表示され、ユーザーが自身の状況を容易に把握できること。
- 一貫性のある体験: AndroidとiOSの両プラットフォームで、同様の操作感と情報構造を提供すること。

## データフロー
基本的なデータフローは以下の通りです。

1. View (Compose/SwiftUI): ユーザー操作をViewModelに通知します。ViewModelから受け取ったUI Stateに基づいて画面を描画します。
2. ViewModel (Android/iOSネイティブ): Viewからのイベントを受け取り、対応するUseCaseを呼び出します。UseCaseからの結果（データやエラー）をUI Stateに変換し、Viewに公開します。
3. UseCase (KMP共通): ViewModelからの要求に基づき、1つまたは複数のRepositoryを呼び出してビジネスロジックを実行します。結果をViewModelに返します。
4. Repository (KMP共通): UseCaseからの要求に基づき、適切なデータソース（RemoteまたはLocal）からデータを取得または保存します。
5. データソース (Remote/Local - KMP共通またはネイティブ):
 - Remote: Ktor Clientを使用してAPIサーバーと通信します。
 - Local (DB): SQLDelightを使用してローカルデータベースにアクセスします。
 - Local (Key-Value): プラットフォーム固有の機能（Android: Jetpack DataStore/SharedPreferences, iOS: UserDefaults）を利用します。これらは`commonMain`でインターフェースを定義し、各ネイティブレイヤーで実装したものを`KmpUseCaseFactory`の初期化時に渡します。

## expect/actual の利用方針
- プラットフォーム固有APIへのアクセスが必要な場合に限定して使用します。
- 可能な限り、`commonMain` でインターフェースを定義し、各ネイティブレイヤーでそのインターフェースを実装する方法を優先的に検討します。
  - 例: Key-Valueストア (SharedPreferences/UserDefaults)
  - `expect`/`actual` は、PlatformFileReaderやSecureStorageのように、システムAPIを直接ラップするような場合に適しています。

## テスト戦略
- 共通ロジック (`shared/commonMain/`): UseCaseやRepositoryの共通部分など、プラットフォームに依存しないロジックに対しては `shared/src/commonTest/` にユニットテストを作成します。
- プラットフォーム固有実装:
  - Android (`shared/androidMain/`, `androidApp/`): `shared/src/androidTest/` および `androidApp/src/test/`, `androidApp/src/androidTest/` にユニットテストやUIテストを作成します。
  - iOS (`shared/iosMain/`, `iosApp/`): `shared/src/iosTest/` および `iosApp/` 内の適切なテストターゲットにユニットテストやUIテストを作成します。
