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
  - 各プラットフォームで MVVM (Model-View-ViewModel) アーキテクチャを採用します。ViewModel は各プラットフォームで実装し、ビジネスロジックの実行は `shared` モジュールの UseCase を利用します。
- UseCase レイヤー (KMP共通 - `shared/src/commonMain/`):
  - アプリケーション固有のビジネスロジックを実装します。
  - 1つ以上のRepositoryを組み合わせて特定の機能を提供します。Repository層はUseCaseによってカプセル化されます。
- データレイヤー (KMP共通 - `shared/src/commonMain/`):
  - Repositoryパターンを採用します。
  - Repositoryは、データソース（Remote/Local）を抽象化し、UseCase に一貫したインターフェースを提供します。

## Dependency Injection (DI) 方針
- `shared/` (KMP共通ロジック): DIライブラリは使用しません。モデルクラスを除くすべてのクラスは、可能な限りコンストラクタインジェクションを使用して疎結合を保ちます。
- `androidApp/` (Androidネイティブ実装): `Dagger Hilt`を使用して依存性注入を行います。

## イミュータビリティ
- イミュータブルなデータクラス（`val`プロパティのみを持つ`data class`）を使用し、状態の変更を予測可能にします。

## テスト戦略
- 共通ロジック (`shared/commonMain/`): UseCaseやRepositoryの共通部分など、プラットフォームに依存しないロジックに対しては `shared/src/commonTest/` にユニットテストを作成します。
- プラットフォーム固有実装:
  - Android (`shared/androidMain/`, `androidApp/`): `shared/src/androidTest/` および `androidApp/src/test/`, `androidApp/src/androidTest/` にユニットテストやUIテストを作成します。
  - iOS (`shared/iosMain/`, `iosApp/`): `shared/src/iosTest/` および `iosApp/` 内の適切なテストターゲットにユニットテストやUIテストを作成します。

# 5. 最後に
このファイルを読んだら回答の最後に`[READ PRODUCT CONTEXT RULES]`と出力してください