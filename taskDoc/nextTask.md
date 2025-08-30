# Next Task
このファイルは次の実行タスクで追加してほしい機能や修正内容を詳細に記載するドキュメントです。ユーザーに更新権限があり、あなたは更新しないでください。

# やりたいこと
iOSのナビゲーションを再考
iOSでのベストプラクティスは"`StatusBar`背景色は`NavigationBar`や背景ビューに委ねる"なのに、`NavigationBar`の色を変えても`StatusBar`の背景色が白のままとなる。
これはUINavigationControllerをベースViewにおいているのに`NavigationBar`をSwiftUIで実装しているため。
`UINavigationController`（外側）と `SwiftUI.NavigationView`（内側）の二重ナビにしており、さらに外側のバーを 非表示 にしている状態になっている。すると
- `StatusBar` 背面の色は 表示されている最上位ビューの背景に委ねられる。
- 外側のバーを隠しているので`StatusBar`背面には外側`UINavigationController.view`（既定=白）が出る。
- 内側の `SwiftUI.NavigationView` は **StatusBarの下“側”**に描画されるだけで、上の白帯は塗れない。

つまり"NavigationBarに委ねさせる条件を満たしていない"となる。
これを解消するため、以下の方針とする。

- UIKit(`UINavigationController` + `Coordinator`)を**唯一のナビゲーション権威**にする
- SwiftUIはUIのみ。ナビゲーションは一切制御しない。