# 1. 基本ルール
- ライブラリの追加はユーザーに一任し、私は追加しません。

# 2. ネイティブ層と共通ロジックの禁止事項・優先順位・スタイルルール
- AndroidとiOSのネイティブ層(`androidApp/`, `iosApp/`)から共通ロジック(`shared/`)にアクセスする際、`model/`は自由に利用して問題ありません。
- UseCaseのオブジェクトは必ず`KmpFactory`クラスから取得します。
  ```kotlin
  private val appSettingUseCase: AppSettingUseCase by lazy {
    KmpFactory.useCaseFactory.appSettingUseCase
  }
  ```
- 共通ロジックである`shared/`は、KotlinのNative実装となります。JVMは使えないため必ずKotlin/Nativeで実装してください。
- `androidApp/`のUI実装はComposeを使用します。
- `iosApp/`のUI実装はSwiftUIを使用します。

# 3. 非同期処理
- `shared/`モジュール内のpublicなI/O処理や時間のかかる処理は、原則として`suspend`関数として実装します。
- Coroutine ScopeとDispatcherの管理には注意が必要です。
  - ViewModel内の処理は、各プラットフォームのViewModelのライフサイクルに紐づくコルーチンスコープ（例: Androidの`viewModelScope`）で実行します。
  - Repository層などでのバックグラウンド処理には、`Dispatchers.IO`や`Dispatchers.Default`などを適切に使用します。

## 4. expect/actual の利用方針
- プラットフォーム固有APIへのアクセスが必要な場合に限定して使用します。
- 可能な限り`commonMain` でインターフェースを定義し、各ネイティブレイヤーでそのインターフェースを実装する方法を優先的に検討します。
  - 例: Key-Valueストア (`SharedPreferences`/`UserDefaults`)
  - `expect`/`actual` は、`PlatformFileReader`や`SecureStorage`のように、システムAPIを直接ラップするような場合に適しています。

# 5. 最後に
このファイルを読んだら回答の最後に`[READ KMP RULES]`と出力してください