# System Patterns

## アーキテクチャ概要
本プロジェクトでは、Kotlin MultiPlatform (KMP) を最大限に活用し、プラットフォーム間で可能な限りコードを共通化することを目指します。主要なアーキテクチャパターンは以下の通りです。

- ネイティブViewレイヤー:
  - Android (`androidApp/`): Jetpack Compose を使用してUIを実装します。
  - iOS (`iosApp/`): SwiftUI を使用してUIを実装します。
  - 各プラットフォームで MVVM (Model-View-ViewModel) アーキテクチャを採用します。ViewModel は各プラットフォームで実装しますが、ビジネスロジックの実行は `shared` モジュールの UseCase を利用します。
- UseCase レイヤー (KMP共通 - `shared/src/commonMain/`):
  - アプリケーション固有のビジネスロジックを実装します。
  - 1つ以上のRepositoryを組み合わせて特定の機能を提供します。
  - ネイティブのViewModelは、`KmpUseCaseFactory` を経由してUseCaseのインスタンスを取得し、ビジネスロジックやデータ取得を実行します。Repository層はUseCaseによってカプセル化されます。
- データレイヤー (KMP共通 - `shared/src/commonMain/`):
  - Repository パターンを採用します。
  - Repository は、データソース（Remote/Local）を抽象化し、UseCase に一貫したインターフェースを提供します。

## モジュール構成
プロジェクトは以下の主要なモジュールで構成されます。

- `shared/`: プラットフォーム共通のロジックを配置します。
  - `src/commonMain/`: プラットフォームに依存しない共有ロジック（Model, Repositoryインターフェース, UseCase, Ktor Client, SQLDelightスキーマ/クエリ, `expect`宣言など）。
  - `src/androidMain/`: Androidプラットフォーム固有の実装（`actual`宣言など）。
  - `src/iosMain/`: iOSプラットフォーム固有の実装（`actual`宣言など）。
- `androidApp/`: Androidアプリケーションモジュール。
- `iosApp/`: iOSアプリケーションモジュール。

## UI実装方針
- Android: Jetpack Compose を全面的に採用します。AndroidViewベースのUI実装は行いません。
- iOS: SwiftUI を全面的に採用します。UIKitの利用やSwiftのinterop層の実装は行いません。

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

## Dependency Injection (DI) 方針
- `shared/` (KMP共通ロジック): DIライブラリは使用しません。
  - モデルクラスを除くすべてのクラスは、可能な限りコンストラクタインジェクションを使用して疎結合を保ちます。
  - UseCaseのインスタンスは、必ず`KmpUseCaseFactory`クラスを経由して取得します。これにより、UseCase以下のRepository層などの依存関係はカプセル化されます。
- `androidApp/` (Androidネイティブ実装): `Dagger Hilt`を使用して依存性注入を行います。

## エラーハンドリング方針
- UseCase層で発生した例外はキャッチし、Kotlin標準の`Result`クラス（またはそれに類するカスタムクラス）を使用して、成功・失敗の状態と結果またはエラー情報をViewModelに明確に伝搬させます。
- ViewModelは受け取った`Result`を解釈し、UIに適したエラーメッセージや状態（ローディング、エラー表示など）に変換してViewに伝えます。

## 非同期処理
- `shared/`モジュール内のpublicなI/O処理や時間のかかる処理は、原則として`suspend`関数として実装します。
- コルーチンスコープとディスパッチャの管理には注意が必要です。
  - ViewModel内の処理は、各プラットフォームのViewModelのライフサイクルに紐づくコルーチンスコープ（例: Androidの`viewModelScope`）で実行します。
  - Repository層などでのバックグラウンド処理には、`Dispatchers.IO`や`Dispatchers.Default`などを適切に使用します。

## イミュータビリティ
- 可能な限りイミュータブルなデータクラス（`val`プロパティのみを持つ`data class`）を使用し、状態の変更を予測可能にします。
