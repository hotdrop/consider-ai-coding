# 概要
このアプリは仮で発行したポイントを獲得したり利用するアプリですが、実態はKotlin MultiPlatform(KMP)でAndroidとiOSのビジネスロジックやデータレイヤーをどのように共通化できるか検証するためのアプリです。TODOアプリだと共通化の有益性がわからなかったので少しフローを複雑にポイント管理のようなアプリとしました。

# ディレクトリ構成
- shared/
  - src/
    - commonMain/  :プラットフォームに依存しない共有ロジック、RepositoryやModelはこのディレクトリに実装してください。"commonMain/"がコアとして機能し、プラットフォーム固有のロジックは"androidMain/"と"iosMain/"に配置されます。
    - androidMain/  : Android 固有のロジックです
    - iosMain/      : iOS 固有のロジックです
- androidApp/  : Androidネイティブ実装です
- iosApp/      : iOSネイティブ実装です

# 画面フローのスクリーンショット
![01_start](./images/01_起動処理フロー.png)  
![02_business](./images/02_ポイント獲得と利用フロー.png)

# 利用ライブラリ
- Ktor: API通信はKtorを使います
- SQLDelight: 履歴の管理に使います

## 実装ガイドライン
- 共通ロジック(commonMain)
  - すべてのpublic関数は"suspend"関数で実装してください。
  - KoinというDIライブラリが標準ですが、Koinを使ってしまうとAndroidで利用しているDaggerHiltと共存させるかAndroidもKoinに統一する必要があり実用性が低下します。そのため、共通ロジックはDIライブラリは使用しません。
  - モデル クラスを除くすべてのクラスは可能な限りコンストラクタインジェクションでクラス間を疎結合に保ってください。
- androidApp, iosApp
  - ネイティブ実装は指示がない限り実装しないでください。

## 機密ファイルの扱い
次のファイルは読み取ったり変更したりしないでください
- .envファイル
- .keystoreファイル
- トークンを含むファイル
- シークレットを含むファイル
- パスワードを含むファイル
