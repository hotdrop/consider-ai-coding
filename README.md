# 概要
このアプリはポイント獲得や利用をするもので、Kotlin MultiPlatform(KMP)を利用しています。KMPを使用し、AndroidとiOSのビジネスロジックやデータレイヤーをどのように共通化できるか検証するためのアプリです。

# ディレクトリ構成
- shared/
  - src/
    - commonMain/  :プラットフォームに依存しない共有ロジック (Model, Repository インターフェース, UseCase, Ktor Client, SQLDelight スキーマ/クエリ, `expect` 宣言など)
    - androidMain/  : Android 固有実装
    - iosMain/      : iOS 固有実装
    - commonTest/: commonMain/ のユニットテスト
    - androidTest/: androidMain/ のユニットテスト、および Android 固有のテスト
    - iosTest/: iosMain/ のユニットテスト、および iOS 固有のテスト
- androidApp/  : Android アプリケーションモジュール
- iosApp/      : iOS アプリケーションモジュール
- gradle/: Gradle 設定関連 ( `libs.versions.toml` を含む)

# 利用ライブラリとバージョン管理
- API通信: API通信は`Ktor`を使います。
- ローカルデータベース: `SQLDelight` ポイント獲得・利用の履歴情報を保持します。
- 非同期処理: `kotlinx.coroutines` (標準ライブラリの一部として積極的に活用)
- テスト: `kotlin.test`, `MockK` (モックが必要な場合)
- バージョン管理: `gradle/libs.versions.toml` を使用して、全ての依存ライブラリのバージョンを一元管理します。

# 設計思想とアーキテクチャ
- 全体
  - KMPを最大限活用し、プラットフォーム間で可能な限りコードを共通化します。
- Viewレイヤー (ネイティブ):
  - Android (`androidApp/`): Android View (XMLレイアウト) およびJetpack Composeを使用して View を実装します。
  - iOS (`iosApp/`): Storyboard または Xib, SwiftUI を使用して View を実装します。
  - MVVM アーキテクチャを採用します。ViewModel は各プラットフォームで実装しますが、ビジネスロジックは `shared` モジュールの UseCase を利用します。
- UseCase レイヤー (KMP共通 - `shared/src/commonMain/`):
  - アプリケーション固有のビジネスロジックを実装します。
  - 1つ以上のUseCaseを組み合わせて特定の機能を提供します。
  - ネイティブとKMPのインタフェースはUseCaseです。`KmpUseCaseFactory`を使ってUseCaseにアクセスします。UseCase移行のRepository層はカプセル化します。
  - ネイティブのViewModelはUseCaseを呼び出してビジネスロジックやデータ取得の実行を行います
- データレイヤー (KMP共通 - `shared/src/commonMain/`):
  - Repository パターンを採用します。
  - Repository は、データソース（Remote/Local）を抽象化し、UseCase に一貫したインターフェースを提供します。
  - Remote: Ktor を使用して API 通信を行います。API クライアントの実装は `shared/src/commonMain/` に配置します。
  - Local:
    - データベース: SQLDelight を使用して、構造化されたデータを永続化します（例: 履歴）。DB スキーマ (`.sq` ファイル) と生成されるインターフェースは `shared/src/commonMain/` に配置します。
    - Key-Valueストア: 設定値などの単純なデータの保存には、プラットフォーム固有の機能（Android: Jetpack DataStore/SharedPreferences, iOS: UserDefaults）を利用します。`expect`/`actual` は使用せず、`commonMain` でインターフェースを定義しそれぞれのネイティブレイヤーで実装し、`KmpUseCaseFactory`の初期化時にオブジェクトを渡します。

# 実装ガイドライン
## コーディングスタイル
Kotlin 公式のコーディング規約 (https://kotlinlang.org/docs/coding-conventions.html) に従います。
## 共通ロジック(`shared/`)
- expect/actual:
  - プラットフォーム固有APIへのアクセスが必要な場合に限定して使用しますが、可能な限りインターフェースと実装の分離で対応できないか検討してください。
  - PlatformFileReader や SecureStorage など、システムAPIをラップしたinterfaceの expect/actual 定義を作る作業に向いています。
  - actual 実装のスケルトンをプラットフォームごとに生成させる時に使えます。
  - iOS側のSwift interopを意識した命名・API設計支援にも使えます。
- 非同期処理: 
  - public な I/O 処理や時間のかかる処理は、原則として `suspend` 関数として実装します。全てのpublic関数は `suspend` 関数で実装します。コルーチンスコープとディスパッチャの管理に注意します (`viewModelScope` はネイティブ側で管理される想定、`Dispatchers.IO`, `Dispatchers.Default` など)。
- `DepencencyInjection`: 
  - `DepencencyInjection`(以下、DI)はKMPにおいてKoinが主流ですが、本プロジェクトではDIライブラリは使用しません。理由はネイティブレイヤーへの影響を極力抑えたいためです。Koinを使ってしまうと例えばAndroidネイティブ側でAppクラスなどにKoinの注入処理を書かねばなりません。しかし既存のAndroidプロジェクトでは`Dagger Hilt`を利用していることが多くDIライブラリを無駄に2つ導入することになってしまいます。従って、共通ロジックではDIライブラリは使用しません。モデルクラスを除くすべてのクラスは可能な限りコンストラクタインジェクションでクラス間を疎結合に保ちます。UseCaseは必ず`KmpUseCaseFactory`クラスを経由して使用します。
- エラーハンドリング: 
  - `Result` クラス (Kotlin標準または自作) の使用を検討し、エラー状態を明確に伝搬させます。UseCase 層で例外をキャッチし、ネイティブの ViewModel 層に分かりやすいエラー情報を提供するように努めます。
- イミュータビリティ: 
  - 可能な限りイミュータブルなデータクラスを使用します。
- モック:
  - API 呼び出しのテストには `FakeHttpClient` や `MockK` を使用します。
- ドキュメンテーション:
  - public なクラス、関数、プロパティには KDoc コメントを記述します。
## Androidネイティブレイヤー(`androidApp/`)
UI実装は必ずComposeを使用してください。AndroidViewベースのUI実装は禁止します。
## iOSネイティブレイヤー(`iosApp/`)
UI実装は必ずSwiftUIを使用してください。Swiftのinterop層の実装は禁止します。(Xcodeでの調整やObjective-C bridgeを考慮できないため)
## ユニットテスト
`commonMain/` のロジック (UseCase, Repository の共通部分など) に対しては `commonTest/` にユニットテストを作成します。プラットフォーム固有の `actual` 実装に対しては、`androidTest/` / `iosTest/` にテストを作成します。

# 画面フローのスクリーンショット
![01_start](./images/01_起動処理フロー.png)  
![02_business](./images/02_ポイント獲得と利用フロー.png)

# 機密ファイルの扱い
次のファイルは読み取ったり変更したりしないでください
- .envファイル
- .keystoreファイル
- トークンを含むファイル
- シークレットを含むファイル
- パスワードを含むファイル
