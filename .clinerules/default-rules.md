# Expected answer
- Please provide the complete implementation code without omitting anything.
- Please provide a detailed explanation in Japanese.
- Please provide a clear justification and reason for the changes and the intention of the implementation.

# 厳守すべき行動指針
- ライブラリ管理について、`gradle/libs.versions.toml` を使用して、全ての依存ライブラリのバージョンを一元管理しています。ライブラリを追加したい場合、必ずユーザーに確認してください。勝手に追加するのは禁止します。

# ネイティブレイヤーと共通ロジックの禁止事項・優先順位・スタイルルール
- AndroidとiOSのネイティブ実装(`androidApp/`, `iosApp/`)から共通ロジック(`shared/`)にアクセスする際、`model/`の中のモデルクラスは自由に利用して問題ありません。
- UseCaseは必ず`KmpUseCaseFactory`クラスを経由して使用します。`KmpUseCaseFactory`クラス以外での使用は禁止します。(例えば、勝手にRepositoryクラスをインスタンス化してはいけません)
- Androidネイティブレイヤー(`androidApp/`)のUI実装において、必ずComposeを使用してください。AndroidViewベースのUI実装は禁止します。
- iOSネイティブレイヤー(`iosApp/`)のUI実装において、必ずSwiftUIを使用してください。Swiftのinterop層の実装は禁止します。(Xcodeでの調整やObjective-C bridgeを考慮できないため)

# 機密ファイルの扱いと `.clineignore`
- 機密情報: `.env` ファイル, `.keystore` ファイル, APIキー、トークン、シークレット、パスワードを含むファイルは絶対に読み取ったり変更したりしません。
- `.clineignore`: プロジェクトルートの `.clineignore` ファイルに記載されているファイルやディレクトリ（ビルド成果物、機密ファイル、OS固有ファイルなど）にはアクセスしません。`list_files` で 🔒 が付いているファイルはアクセス禁止です。
