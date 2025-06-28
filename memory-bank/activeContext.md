# HttpClient実装の分離計画

## 目的

`KmpUseCaseFactory`に集中している`HttpClient`の生成と設定の責務を、新しく作成する`HttpClientFactory`に分離する。これにより、各クラスの責務を明確にし、コードの可読性、メンテナンス性、再利用性を向上させる。

## 計画の詳細

### Step 1: `HttpClientFactory`の作成

`HttpClient`のインスタンス化と設定に特化した`HttpClientFactory`オブジェクトを作成する。

-   **ファイルパス:** `shared/src/commonMain/kotlin/jp/hotdrop/considercline/repository/remote/HttpClientFactory.kt`
-   **実装内容:**
    -   `KmpUseCaseFactory`から`ktorNativeClient`の生成ロジックを移動する。
    -   ビルド構成（デバッグ/リリース）に応じて設定を切り替えられるように、`isDebug`フラグを受け取る`create()`メソッドを定義する。

```kotlin
// shared/src/commonMain/kotlin/jp/hotdrop/considercline/repository/remote/HttpClientFactory.kt

package jp.hotdrop.considercline.repository.remote

import io.ktor.client.HttpClient as KtorNativeClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory {

    fun create(isDebug: Boolean): KtorNativeClient {
        return KtorNativeClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = if (isDebug) LogLevel.ALL else LogLevel.NONE
            }
        }
    }
}
```

### Step 2: `KmpUseCaseFactory`の修正

`KmpUseCaseFactory`が新しい`HttpClientFactory`を利用するように修正する。

-   `KmpUseCaseFactory`内の`ktorNativeClient`プロパティの定義を削除する。
-   `HttpClientFactory.create()`を呼び出して`KtorNativeClient`を取得し、`KtorHttpClient`に渡すように変更する。

```kotlin
// shared/src/commonMain/kotlin/jp/hotdrop/considercline/di/KmpUseCaseFactory.kt の修正案

// ... imports ...
import jp.hotdrop.considercline.repository.remote.HttpClientFactory // 追加
import jp.hotdrop.considercline.repository.remote.KtorHttpClient // 追加

object KmpUseCaseFactory {
    // ...
    
    // HttpClientの生成ロジックを削除
    
    // API
    // isDebugはビルド時に適切に設定されることを想定
    private val ktorNativeClient: KtorNativeClient by lazy { HttpClientFactory.create(isDebug = true) }
    private val httpClient: HttpClient by lazy { KtorHttpClient(ktorNativeClient) }
    // private val httpClient: HttpClient by lazy { FakeHttpClient(sharedPreferences) } // Fakeは必要に応じて切り替え
    private val pointApi: PointApi by lazy { PointApi(httpClient) }
    private val userApi: UserApi by lazy { UserApi(httpClient) }

    // ...
}
```

### 変更後のクラス構造

```mermaid
classDiagram
    class HttpClientFactory {
        +create(isDebug: Boolean): KtorNativeClient
    }
    class KmpUseCaseFactory {
        -httpClient: HttpClient
        +appSettingUseCase
        +pointUseCase
        +historyUseCase
    }
    class PointApi {
        -httpClient: HttpClient
    }
    KmpUseCaseFactory ..> HttpClientFactory : uses
    KmpUseCaseFactory --|> PointApi : provides dependency
    note for HttpClientFactory "HttpClientの生成と設定の責務を持つ"
    note for KmpUseCaseFactory "依存性の組み立てに集中"
