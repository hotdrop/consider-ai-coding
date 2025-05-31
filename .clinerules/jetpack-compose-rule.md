# Project Architecture and Best Practices
1. マテリアルデザイン2のガイドラインとコンポーネントに従う
2. 非同期操作には`Kotlin Coroutine`と`Flow`を使用する
3. 各機能は、1つのActivity、1つのViewModel、1つのNavigationHost、画面数分のScreen関数(Composeで実装)という構成にすること。
   1. ポイント獲得（PointGet）機能の例: 
      1. Activity: PointGetActivity.kt
      2. ViewModel: PointGetViewModel.kt
      3. NavigationHost: PointGetNavigationHost.kt
      4. 画面UI: PointGetInputScreen, PointGetConfirmScreen
      5. 画面遷移: ポイント入力画面 -> ポイント確認画面
4. Composeで作成した画面は`NavHostController`を使って画面遷移する。
5. ViewModelは`Dagger Hilt`を使用してDIする。Activityスコープで生存させ、Composeで実装された各Screenの状態は全てViewModelで持つ。画面遷移時に引数を渡すことは基本しない。ただし、可読性や保守性を加味してそちらの方が良いと判断した場合は引数で渡す実装をしても良い。
6. `NavHostController`のコンストラクタ引数で`hiltViewModel()`を使いViewModelを定義する。
7. ViewModelと`UI State`を使用して単方向データフローに従う。

# Compose UI Guideline
1. remember と derivedStateOf を適切に使用する
2. 1つの関数に全てのコンポーネントを実装するのではなく、適切に意味のある単位で分けること。例えば概要ラベルとTextFieldはそれぞれ関数を分けて可読性を保つ
3. Compose 修飾子の順序を適切に使用する
4. Compose 可能な関数の命名規則に従う規約
5. 適切な`@Preview(showBackground = true)`を実装する
6. MutableState による適切な状態管理を使用する
7. 適切なエラー処理と読み込み状態を実装する
8. `Activity`でComposeで実装する画面をsetContentする場合、必ず`ConsiderClineTheme`を使用する
9. アクセシビリティガイドラインに従う
10. 適切なアニメーションパターンを実装する

# edge-to-egde対応
基本的に画面のcontents部分は以下のBoxで括り`fillMaxSize`を指定してedge-to-edge対応すること
```kt
Box(
    modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
        .padding(paddingValues)
)
```

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
