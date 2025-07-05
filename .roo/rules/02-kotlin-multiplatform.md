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

# 3. 最後に
このファイルを読んだら回答の最後に`[READ KMP RULES]`と出力してください