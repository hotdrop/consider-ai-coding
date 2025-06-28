# HttpClient リファクタリング計画

ユーザーのフィードバックと `sampleCode` に基づき、合意されたHttpClientのリファクタリング計画。

## 中核目標
1.  **`HttpClient` ラッパーの導入:** `sampleCode/HttpClient.kt` に示されているKtorクライアントのラッパークラスを導入する。
2.  **`HttpClientFactory` の強化:** `sampleCode/HttpClientFactory.kt` に合わせて、`defaultRequest` 設定などを含むように `HttpClientFactory` を更新する。
3.  **ネイティブからのDI:** `apiEntryPoint` と `isDebug` フラグをネイティブのAndroidおよびiOS層から提供する。
4.  **DI構造の維持:** `KmpUseCaseFactory` を中心とした既存のDI構造を維持する。

## 実装ステップ

1.  **`PlatformDependencies` の拡張:** `KmpUseCaseFactory.kt` の `PlatformDependencies` インターフェースに `apiEntryPoint: String` と `isDebug: Boolean` プロパティを追加する。
2.  **ネイティブ層での実装:** `AndroidPlatformDependencies.kt` と `IosPlatformDependencies.swift` で具体的な `apiEntryPoint` と `isDebug` の値を実装する。
3.  **`HttpClientFactory` の更新:** `create` メソッドが `apiEntryPoint` と `isDebug` を受け取り、`defaultRequest` とロギングレベルを設定するように変更する。
4.  **`HttpClient` と `KtorHttpClient` のリファクタリング:**
    *   `HttpClient` インターフェースを更新する。
    *   `sampleCode` のロジックを `KtorHttpClient` に実装する。
5.  **`KmpUseCaseFactory` の更新:** `PlatformDependencies` から新しい `apiEntryPoint` と `isDebug` を利用し、新しい `HttpClient` を正しくインスタンス化するようにDIロジックを調整する。

## 依存関係のフロー図
```mermaid
graph TD
    subgraph "ネイティブ層 (Android/iOS)"
        A[AndroidPlatformDependencies] -->|apiEntryPoint, isDebugを提供| C{KmpUseCaseFactory};
        B[IosPlatformDependencies] -->|apiEntryPoint, isDebugを提供| C;
    end

    subgraph "共通層"
        C -- "1. init(dependencies)" --> C;
        C -- "2. creates" --> D[HttpClientFactory];
        D -- "3. create(apiEntryPoint, isDebug)" --> E[KtorNativeClient];
        C -- "4. KtorNativeClientで生成" --> F(KtorHttpClient);
        F -- "implements" --> G(HttpClient Interface);
        C -- "5. 注入" --> H[PointApi / UserApi];
        H -- "依存" --> G;
    end
