# Next Task
このファイルは次の実行タスクで追加してほしい機能や修正内容を詳細に記載するドキュメントです。ユーザーに更新権限があり、あなたは更新しないでください。

# やりたいこと
JWTを使った認証方式に対応したい。具体的には以下の対応をしてほしい
- ユーザー作成API`UserApi.kt`のcreate関数で実行するAPIのResponseにJWTとrefreshTokenを含める
  - JWTとrefreshTokenは`KmpSharedPreferences`に保持する
- 再認証APIを用意する
- 他のAPIを実行する場合はHeaderに`Authorization: Bearer <JWT>`をつけて実行する
  - ktorのPluginを使う
  - JWTの慣習にしたがって401が返ってきたら再認証をして新しいJWTを保持し元のリクエストを再送すること
- 注意点ですが、`UserApi.kt`のcreate関数で実行するAPIのみ`Authorization`の認証は不要です。