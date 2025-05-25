# Koin除去 実装計画

## 1. はじめに

このドキュメントは、KMPプロジェクトの共通ロジック (`shared` モジュール) からDIライブラリであるKoinを除去し、手動DI (コンストラクタインジェクション) に置き換えるための計画を記述するものです。

## 2. 背景と目的

現状、`shared` モジュールでは依存性注入のためにKoinが利用されています。
DIライブラリは便利な一方で、以下の点を考慮し、今回はKoinを除去する方針とします。

* 学習コスト: 新しいメンバーがプロジェクトに参加する際に、Koinの学習コストが発生します。
* ビルド時間: DIライブラリがビルド時間に影響を与える可能性があります。
* シンプルさ: 小規模から中規模のプロジェクトにおいては、手動DIでも十分に管理可能であり、コードの透明性が向上する場合があります。
* プロジェクトルール: プロジェクトのルールとして「DIライブラリは使用しない」と明記されています。

本タスクの目的は、Koinへの依存をなくし、コンストラクタインジェクションを基本とした手動DIに移行することで、コードのシンプル化とプロジェクトルールへの準拠を目指します。

## 3. 作業タスク一覧

Koin除去は以下のタスクに分割して進めます。

### 3.1. 準備・分析フェーズ

* T1: Koin利用状況の調査:
    * `shared` モジュール内のどこでKoinのモジュール定義 (`module { ... }`) が行われているかを確認します。
        * 対象ファイル例:
            * [`shared/src/commonMain/kotlin/jp/hotdrop/considercline/di/DatabaseModule.kt`](shared/src/commonMain/kotlin/jp/hotdrop/considercline/di/DatabaseModule.kt)
            * [`shared/src/commonMain/kotlin/jp/hotdrop/considercline/di/NetworkModule.kt`](shared/src/commonMain/kotlin/jp/hotdrop/considercline/di/NetworkModule.kt)
            * [`shared/src/commonMain/kotlin/jp/hotdrop/considercline/di/RepositoryModule.kt`](shared/src/commonMain/kotlin/jp/hotdrop/considercline/di/RepositoryModule.kt)
            * [`shared/src/androidMain/kotlin/jp/hotdrop/considercline/di/AndroidModule.kt`](shared/src/androidMain/kotlin/jp/hotdrop/considercline/di/AndroidModule.kt)
            * [`shared/src/iosMain/kotlin/jp/hotdrop/considercline/di/IosModule.kt`](shared/src/iosMain/kotlin/jp/hotdrop/considercline/di/IosModule.kt)
    * Koinの `get()` や `inject()` などで依存性を取得している箇所を特定します。
    * テストコードでのKoin利用箇所 (例: `KoinTest`, `declareMock`) を特定します。
* T2: 依存関係の洗い出し:
    * Koinモジュールで定義されている各クラス間の依存関係を明確にします。
    * どのクラスがどのクラスを必要としているかをリストアップします。

### 3.2. Koin依存の除去フェーズ

* T3: Koinライブラリの削除:
    * [`gradle/libs.versions.toml`](gradle/libs.versions.toml) からKoin関連のライブラリ定義を削除します。
    * [`shared/build.gradle.kts`](shared/build.gradle.kts) の `dependencies` ブロックからKoin関連の依存を削除します。
* T4: Koinモジュールファイルの削除:
    * `shared/src/commonMain/kotlin/jp/hotdrop/considercline/di/` ディレクトリ内のKoinモジュール定義ファイル群を削除します。
    * `shared/src/androidMain/kotlin/jp/hotdrop/considercline/di/AndroidModule.kt` を削除します。
    * `shared/src/iosMain/kotlin/jp/hotdrop/considercline/di/IosModule.kt` を削除します。

### 3.3. 手動DIへの移行フェーズ (共通ロジック)

* T5: コンストラクタインジェクションへの変更:
    * Koinによって注入されていた依存関係を、各クラスのコンストラクタ引数で受け取るように変更します。
    * 対象クラス例:
        * Repository実装クラス (例: `AppSettingRepositoryImpl`, `HistoryRepositoryImpl`, `PointRepositoryImpl`)
        * UseCaseクラス (もしあれば)
        * APIクライアント (例: `KtorHttpClient`)
        * DAO (例: `HistoryDao`, `SettingDao`)
* T6: 依存性解決のためのファクトリ/初期化処理の実装:
    * `shared` モジュール内で、依存関係のグラフを構築し、インスタンスを生成・提供するためのファクトリメソッドや初期化ブロックを実装します。
    * プラットフォーム固有の依存性 (`DriverFactory` など) は、`expect`/`actual` の仕組みを利用してインスタンスを取得し、共通ロジックのファクトリに渡す形を検討します。
    * 例えば、`shared` モジュールのエントリーポイントとなるようなクラスや、依存関係のルートとなるオブジェクトを生成する場所で、手動でインスタンスを組み立てます。
    * 注意: ネイティブ層 (`androidApp`, `iosApp`) での最終的なインスタンス生成とViewModelへの注入は、私の担当範囲外です。`shared` モジュール内で完結する依存関係の構築と、ネイティブ層から利用しやすいインターフェースを提供することに注力します。

### 3.4. テストコード修正フェーズ

* T7: ユニットテストの修正:
    * Koinに依存していたテストコードを修正します。
    * `declareMock` やKoinのテスト用モジュールを利用していた箇所は、テスト対象クラスのインスタンス生成時に直接モックオブジェクトをコンストラクタ経由で注入するように変更します。
    * 必要に応じて、テスト用のファクトリメソッドを作成します。

### 3.5. ドキュメント・確認フェーズ

* T8: 変更内容のドキュメント化:
    * 本ドキュメント (このファイル) に、実際に行った変更の概要や、手動DIの構成方法について追記・修正します。
* T9: ビルドとテストの実行:
    * プロジェクト全体をビルドし、すべてのユニットテストが成功することを確認します。

## 4. 影響範囲

* `shared` モジュール全体 (commonMain, androidMain, iosMain)
    * DI関連のコード (`di` パッケージ)
    * Repository, UseCase, APIクライアントなどの各クラスのコンストラクタ
    * テストコード
* `gradle/libs.versions.toml`
* `shared/build.gradle.kts`

## 5. リスクと対策

* リスク: 依存関係の見落としや、手動でのインスタンス生成順序の誤りによる実行時エラー。
    * 対策: T2での依存関係の洗い出しを慎重に行います。変更後は十分なテストを実施します。
* リスク: テストコードの修正漏れ。
    * 対策: T9で全てのテストがパスすることを確認します。

## 6. 今後のステップ

1.  この計画書を `KOIN_REMOVAL_PLAN.md` としてプロジェクトルートに出力します。
2.  ユーザーに計画内容を確認していただきます。
3.  承認後、`code` モードに切り替えて、上記のタスクT1から順次実装を開始します。

