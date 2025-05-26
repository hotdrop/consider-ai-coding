# Tech Context

## 主要言語
- Kotlin: プロジェクト全体で主要な開発言語として使用します。KMPの特性を活かし、共通ロジックからネイティブ実装まで一貫してKotlinを用います。

## フレームワーク・ライブラリ
### KMP共通 (`shared/`)
- Kotlin MultiPlatform (KMP): AndroidとiOSでビジネスロジックとデータレイヤーを共通化するための基盤技術。
- Ktor: API通信を行うためのHTTPクライアントライブラリ。`shared/src/commonMain/` にクライアント実装を配置します。
- SQLDelight: ローカルデータベースを扱うためのライブラリ。型安全なSQLクエリを生成し、`shared/src/commonMain/` にDBスキーマ (`.sq`ファイル) と生成インターフェースを配置します。
- kotlinx.coroutines: 非同期処理を実現するための標準ライブラリ。`suspend`関数やFlowを積極的に活用します。
- kotlin.test: `commonMain/` のロジックに対するユニットテストを作成するためのライブラリ。
- MockK: モックが必要な場合のテストに使用するライブラリ。

### Androidネイティブ (`androidApp/`)
- Jetpack Compose: UI実装のための宣言的UIツールキット。Material Design 3ガイドラインに準拠します。
- Dagger Hilt: 依存性注入 (DI) を行うためのライブラリ。
- Android Jetpack Libraries: ViewModel, LiveData (またはStateFlow), Navigation Component (Compose Navigation) など、モダンなAndroidアプリ開発に必要な各種ライブラリ。
- AndroidX Test, Espresso, Robolectric: Android固有のテスト（UIテスト、インテグレーションテスト）に使用します。

### iOSネイティブ (`iosApp/`)
- SwiftUI: UI実装のための宣言的UIフレームワーク。
- XCTest: iOS固有のテスト（UIテスト、ユニットテスト）に使用します。

## バージョン管理
- 全ての依存ライブラリのバージョンは、プロジェクトルートの `gradle/libs.versions.toml` ファイルで一元管理します。
- ライブラリを追加・更新する際は、必ずこのファイルを編集し、ユーザーに確認を取ります。勝手な追加は行いません。

## コーディング規約
- Kotlin公式のコーディング規約 (https://kotlinlang.org/docs/coding-conventions.html) に準拠します。
- publicなクラス、関数、プロパティにはKDocコメントを記述します。

## テスト戦略
- 共通ロジック (`shared/commonMain/`): UseCaseやRepositoryの共通部分など、プラットフォームに依存しないロジックに対しては `shared/src/commonTest/` にユニットテストを作成します。
- プラットフォーム固有実装:
- Android (`shared/androidMain/`, `androidApp/`): `shared/src/androidTest/` および `androidApp/src/test/`, `androidApp/src/androidTest/` にユニットテストやUIテストを作成します。
- iOS (`shared/iosMain/`, `iosApp/`): `shared/src/iosTest/` および `iosApp/` 内の適切なテストターゲットにユニットテストやUIテストを作成します。
- API呼び出しのテストには、`FakeHttpClient` (テスト用の偽実装) や `MockK` を使用します。

## 機密ファイルの扱いと `.clineignore`
- 以下の情報は機密情報として扱い、読み取りや変更は行いません。
- `.env` ファイル
- `.keystore` ファイル
- APIキー、トークン、シークレット、パスワードを含むファイル
- プロジェクトルートの `.clineignore` ファイルに記載されているファイルやディレクトリ（ビルド成果物、機密ファイル、OS固有ファイルなど）にはアクセスしません。`list_files` で 🔒 が付いているファイルはアクセス禁止です。

## Jetpack Compose ガイドライン (Android)
`.clinerules/jetpack-compose-rule.md` に記載されている以下のガイドラインに従います。
- Project Architecture and Best Practices: クリーンアーキテクチャ、単方向データフロー、状態ホイスティングなど。
- Folder Structure: `androidApp/src/main/java/jp/hotdrop/considercline/android` 以下に `data/`, `ui/` などを配置。
- Compose UI Guidelines: `remember`, `derivedStateOf` の適切な使用、リコンポジション最適化、Modifierの順序など。
- Testing Guidelines: ViewModel/UseCaseのユニットテスト、ComposeテストフレームワークによるUIテスト。
- Performance Guidelines: リコンポジション最小化、Lazyリストの適切な使用、効率的な画像読み込みなど。

## `expect`/`actual` の利用方針
- プラットフォーム固有APIへのアクセスが必要な場合に限定して使用します。
- 可能な限り、`commonMain` でインターフェースを定義し、各ネイティブレイヤーでそのインターフェースを実装する方法を優先的に検討します。
- 例: Key-Valueストア (SharedPreferences/UserDefaults)
- `expect`/`actual` は、PlatformFileReaderやSecureStorageのように、システムAPIを直接ラップするような場合に適しています。
