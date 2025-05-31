# Cline Rules for E2E Test Server Project
このファイルは、Clineがこのプロジェクトで開発を行う際に従うべきルールを定義します。

## 1. 全体のルール
- 必ず日本語で回答してください。
- 日本語で詳細な説明を提供してください。
- 実装コードを省略なくすべて提供してください。
- 変更の正当性と理由、および実装の意図を明確に説明してください。
- ソフトウェア開発の基本原則を遵守しましょう。DRY原則、YAGNI、SOLID原則などです。ただし、原則よりもAndroidやKotlin、iOSやSwiftの慣習を優先して問題ありません。

## 2. 厳守すべき行動指針
- 「タスクを確認してください」と指示されたら`./nextTask.md`を確認し、Rulesに従って実装計画を立て`memory-bank/activeContext.md`を更新してください。
- ライブラリを勝手に追加することは禁止します。ライブラリ管理は`gradle/libs.versions.toml` を使用して一元管理しているためこのファイルをReadすることは問題ありませんが、追加が必要な場合は必ずユーザーに確認してください。

## 3. ネイティブレイヤーと共通ロジックの禁止事項・優先順位・スタイルルール
- AndroidとiOSのネイティブ実装(`androidApp/`, `iosApp/`)から共通ロジック(`shared/`)にアクセスする際、`model/`の中のモデルクラスは自由に利用して問題ありません。
- UseCaseは必ず`KmpUseCaseFactory`クラスを経由して使用します。`KmpUseCaseFactory`クラス以外での使用は禁止します。(例えば、勝手にRepositoryクラスをインスタンス化してはいけません)
- Androidネイティブレイヤー(`androidApp/`)のUI実装において、必ずComposeを使用してください。AndroidViewベースのUI実装は禁止します。
- iOSネイティブレイヤー(`iosApp/`)のUI実装において、必ずSwiftUIを使用してください。Swiftのinterop層の実装は禁止します。(Xcodeでの調整やObjective-C bridgeを考慮できないため)

## 4. コーディング規約
- Kotlin公式のコーディング規約 (https://kotlinlang.org/docs/coding-conventions.html) に準拠します。
- publicなクラス、関数、プロパティにはKDocコメントを記述します。
- 共通ロジックである`shared/`は、KotlinのNative実装となります。JVMは使えないため必ずKotlin/Nativeで実装してください。
- Androidネイティブである`androidApp/`は、Android SDK下で動作するKotlin実装となります。Androidの慣習に従いつつ、Kotlinらしい書き方で実装してください。
- iOSネイティブである`iosApp`は、Swift実装となります。iOSの慣習に従い、Swiftらしい書き方で実装してください。

## 5. Memory Bankの利用
- ユーザーが質問した際、必ず`memory-bank/`ディレクトリ内のすべてのMarkdownファイルを読み込み、現在のタスク状況とコンテキストを完全に理解してください。
- 実装計画を立て終わり実装フェーズに進む際、または実装タスクが完了した時、必ず`activeContext.md`に現在の作業状況を漏れなく反映してください。

## 6. 機密ファイルの扱いと.clineignoreについて
- 機密情報: `.env` ファイル, `.keystore` ファイル, APIキー、トークン、シークレット、パスワードを含むファイルは絶対に読み取ったり変更したりしません。
- `.clineignore`: プロジェクトルートの `.clineignore` ファイルに記載されているファイルやディレクトリ（ビルド成果物、機密ファイル、OS固有ファイルなど）にはアクセスしません。`list_files` で 🔒 が付いているファイルはアクセス禁止です。
