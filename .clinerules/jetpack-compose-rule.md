# Project Architecture and Best Practices
1. マテリアルデザイン2のガイドラインとコンポーネントに従う
2. 非同期操作には`Kotlin Coroutine`と`Flow`を使用する
3. 各機能は1つのActivityと1つのViewModel、それに対して画面数だけComposeでScreen関数を作る。例えばポイント獲得（PointGet）画面の場合は以下のような構成となる
   1. 画面構成数: 2つ
   2. 画面遷移: ポイント入力画面 -> ポイント確認画面
   3. 実装するクラスまたは関数
      1. Activity: PointGetActivity
      2. ViewModel: PointGetViewModel
      3. Composeで作成する画面: PointGetInputScreen, PointGetConfirmScreen
4. Composeで作成した画面は`NavHostController`を使って画面遷移する
5. ViewModelは`Dagger Hilt`を使用して依存性注入を実装する。具体的には、各Compose画面(Screen)のコンストラクタ引数で`hiltViewModel()`を使って利用する
6. ViewModelと`UI State`を使用して単方向データフローに従う
7. 適切な`State hoisting`と`composition`を実装する

# Compose UI Guideline
1. remember と derivedStateOf を適切に使用する
2. 適切な再コンポジション最適化を実装する
3. Compose 修飾子の順序を適切に使用する
4. コンポーズ可能な関数の命名規則に従う規約
5. 適切なプレビューアノテーションを実装する
6. MutableState による適切な状態管理を使用する
7. 適切なエラー処理と読み込み状態を実装する
8. `Activity`でComposeで実装する画面をsetContentする場合、必ず`ConsiderClineTheme`を使用する
9. アクセシビリティガイドラインに従う
10. 適切なアニメーションパターンを実装する

# Test Guideline
1. Compose テストフレームワークを使用して UI テストを実装する

# Performance Guideline
1. 適切なキーを使用して再コンポジションを最小限に抑える
2. LazyColumn と LazyRow による適切な遅延読み込みを使用する
3. 効率的な画像読み込みを実装する
4. 不要な更新を防ぐために適切な状態管理を使用する
5. 適切なライフサイクル認識に従う
6. 適切なメモリ管理を実装する
7. 適切なバックグラウンド処理を使用する

# Folder Structure
```
androidApp/
  src/
    main/
      java/jp/hotdrop/considercline/android
        data/
        ui/
          pointGet/
          home/
          start/
          theme/
      res/
        drawable/
        layout/
        mipmap/
        values/
    test/
    androidTest/
```
