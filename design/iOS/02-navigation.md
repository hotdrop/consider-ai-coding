# Navigationの運用ルール
## 1. ViewControllerは1フロー = 1 HostingController
- 既存ViewController（Coordinator）が `UIHostingController(rootView: AFlowRootView)` を push/present。
- やってはいけない: A→B→C の各画面を 別々の HostingController で push（スタックが二重化して崩れます）。

## 2. フロー内の画面遷移は SwiftUI 側だけで完結
- 画面A → 画面B → 画面C は SwiftUI の `NavigationStack`（iOS16+）/ `NavigationPath` を使う。
- 画面間の引数は init で渡すか、`ObservableObject` 経由で注入。

## 3. UIKit からの介入は“意図”ベース
- ViewControllerがフロー内に口を出すのは「reset/popToRoot/DeepLink適用」等の 明確な意図に限る。
- 共有オブジェクト(例：`NavigationCoordinator` 的な `ObservableObject`)の `path` をViewControllerが直接操作して適用（iOS16+）。
- SwiftUI↔UIKitは enumベースのIntent（`case didFinish`, `case needsLogin` など）をクロージャ/Subjectで橋渡し。

## 4. フローからの"脱出"はUIKitの責務
- SwiftUI側は「閉じたい」「別機能へ行きたい」という Intent だけ送る。ViewController（Coordinator）が `dismiss` / 別フローの起動を実行。
- こうすると戻る導線（SwipeBack, NavigationBar）は一貫して効く。

# 具体的にどう分けるか
- ViewControllerの責務
  - フロー開始/終了、modal or push選択、DeepLink適用（`path` 初期構築）。
  - 例：ログイン済みなら `画面A → 画面B` へ直接入れるように `path` をセットしてから HostingController を出す。
- SwiftUIの責務
  - 画面A/B/CのUIと遷移（`path.append(.b)` のような操作）。
  - 例外（ログイン要求、外部設定、他タブ遷移など）は `Intent通知` に変換してViewControllerへ。

# OSバージョン差分
`NavigationStack`を採用したいがiOS16+のため、iOS15をサポートする現場では利用不可。したがって、`NavigationView` + `NavigationLink`を使う。基本は「ViewController=フロー開始/終了」「フロー内=SwiftUIで遷移」
どうしてもViewControllerが介入したい場合は 各画面を別HostingControllerでpush しかなく、アンチパターン寄り（戻り操作やState保持で崩れやすい）。この世代は "ViewControllerは入口/出口のみ" を強く推奨。

# まとめ（意思決定）
ViewControllerはフローの入口/出口のみを制御し、画面A → 画面B → 画面Cの遷移はSwiftUI側で完結。
