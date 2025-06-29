# JWT認証方式の導入

## 1. KmpSharedPreferencesの更新
- JWTとリフレッシュトークンを保存・取得・削除する関数を追加済み。

## 2. APIレスポンスモデルの修正
- `UserResponse`に`jwt`と`refreshToken`プロパティを追加済み。

## 3. UserApiの修正
- `create`関数の戻り値を`UserResponse`に変更済み。
- `UserResponse.mapper`を削除済み。

## 4. AppSettingRepositoryの修正
- `registerUser`関数でJWTとリフレッシュトークンを`KmpSharedPreferences`に保存する処理を追加済み。

## 5. RepositoryFactoryの修正
- `AppSettingRepository`のインスタンス生成時に`KmpSharedPreferences`を渡すように修正済み。

## 6. KmpFactoryの修正
- `RepositoryFactoryImpl`のインスタンス生成時に`KmpSharedPreferences`を渡すように修正済み。

## 7. AuthApiの追加
- リフレッシュトークンAPIを呼び出すための`AuthApi.kt`を新規作成済み。
- `RefreshTokenRequest.kt`と`RefreshTokenResponse.kt`を新規作成済み。

## 8. HttpClientFactoryの修正
- Ktorの`Auth`プラグインを組み込み、JWT認証とリフレッシュトークンによる再認証ロジックを実装済み。
- `UserApi.create`エンドポイントは認証対象から除外済み。

---
# HttpClientリファクタリング計画

## 1. 現状のアーキテクチャと問題点