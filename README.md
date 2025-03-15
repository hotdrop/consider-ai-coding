# clnerulesの日本語版

# 期待する回答
- 実装コードは省略せず、完全な形で提供してください
- 日本語での詳細な説明をしてください

# 設計書
## 概要
このアプリはProductFlavorによってコーヒーまたは紅茶のデザインでポイント獲得したり使用するアプリです。Kotlin MultiPlatform(KMP)でどこまでAndroidとiOSのコードを共通化できるかを検証するためのサンプルアプリになるため、実際に外部APIは実行せずFake HttpClientを使います。
sampleprj ディレクトリに入っているDartコードは、Flutterで作成されたこのアプリの完成版です。このアプリをKMPを使用して Android および iOS アプリケーションに移行してください。
なお、KMPの範囲はビジネスロジックに限定し、UIレイヤーはAndroidとiOSネイティブコードとして実装します。

## ディレクトリ構成
- shared/src
  - commonMain/  :プラットフォームに依存しない共有ロジック、RepositoryやModelはこのディレクトリに実装してください。"commonMain/"がコアとして機能し、プラットフォーム固有のロジックは"androidMain/"と"iosMain/"に配置されます。
  - androidMain/  : Android 固有のロジックです
  - iosMain/      : iOS 固有のロジックです
- androidApp/  : Androidネイティブ実装、UIレイヤーはここに実装してください
- iosApp/      : iOSネイティブ実装、UIレイヤーはここに実装してください
- sampleprj/   : Flutterで作られたアプリの完成版です。このプロジェクトのdartコードを参考に共通ロジック、Android、iOSのコードを実装してください。

## アーキテクチャ
sampleprj ディレクトリの構成と同様、ローカルストレージや外部からデータを取得する処理はリポジトリパターンを採用し、UIレイヤーはMVVMを採用してください。

## 利用ライブラリ
sampleprj/ ディレクトリのFlutterコードで使われている各ライブラリのうち、共通ロジック(commonMain)ではどのライブラリを使って移行してほしいか記載します
- API
  - dio -> Ktor
- ローカル ストレージ
  - Hive -> SQLDelight
  - shared_preferences -> multiplatform-settings
- 依存性注入 (DI)
  - Riverpod -> Koin

## 実装ガイドライン
- 共通ロジック(commonMain)
  - FakeHttpClient を使用して API 呼び出しをモックします。
  - モデル クラスを除くすべてのクラスは、依存性注入に Koin を使用してください
  - すべてのpublic関数は"suspend"関数で実装してください。
- androidApp
  - JetpackComposeを使ってUIを実装してください
  - ViewModelにはJetpackのViewModelを使用してください
  - ViewとViewModelはFlowを使ってください。

## 機密ファイルの扱い
次のファイルは読み取ったり変更したりしないでください
- .envファイル
- .keystoreファイル
- トークンを含むファイル
- シークレットを含むファイル
- パスワードを含むファイル
