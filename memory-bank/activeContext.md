# 実装計画概要

## 目的
`nextTask.md`に記載された内容に基づき、iOSアプリにホーム画面を実装し、既存画面からの導線を確立する。

## 詳細
1. **ホーム画面の作成**:
    - `HomeView.swift`ファイルを作成し、ホーム画面のUIを実装する。
    - UIはAndroid版の`androidApp/src/main/java/jp/hotdrop/considercline/android/ui/home/HomeActivity.kt`を参考にする。
    - 具体的には、Image画像、ラベル、配置などをAndroid版に合わせるが、使用するコンポーネントはiOSの標準に準拠する。

2. **導線の実装**:
    - `MainView.swift`および`StartView.swift`から新しく作成する`HomeView.swift`へのナビゲーション導線を実装する。

3. **リソースの利用**:
    - カラー、文字列定義、必要なアセットはすべて既存のプロジェクトリソースから使用する。
    - 新しいアセットの追加は行わない。もし不足しているアセットがある場合は、ユーザーに確認を求める。

## 詳細サブタスクリスト

### 1. ホーム画面の作成
- [x] `HomeViewModel.swift`ファイルの作成
- [x] `HomeView.swift`ファイルの作成
- [x] `HomeView.swift`にUIコンポーネント（Image、ラベルなど）を実装
- [x] Android版 (`HomeActivity.kt`) を参考にUIレイアウトを調整
- [x] iOS標準コンポーネントの使用を徹底

### 2. 導線の実装
- [x] `MainView.swift`から`HomeView.swift`へのナビゲーション導線を実装
- [x] `StartView.swift`から`HomeView.swift`へのナビゲーション導線を実装

### 3. リソースの利用
- [x] 既存のカラーリソースの利用
- [x] 既存の文字列定義の利用
- [x] 既存のアセットの利用

### 4. HomeView/ViewModelの追加実装
- [x] `HomeViewModel.swift`に`PointUseCase`と`HistoryUseCase`を注入する
- [x] `iosApp/iosApp/Sources/usecase/` に `PointUseCaseProtocol.swift` を作成する
- [x] `iosApp/iosApp/Sources/usecase/` に `HistoryUseCaseProtocol.swift` を作成する
- [x] `HomeViewModel.swift`でポイント残高と利用履歴を読み込むロジックを実装する
- [x] `HomeViewModel.swift`の`HomeViewState`にポイント残高と利用履歴の情報を追加する
- [x] `HomeView.swift`で`HomeViewModel`からポイント残高を取得し表示する
- [x] `HomeView.swift`で`HomeViewModel`から利用履歴を取得し表示する
- [x] `HomeView.swift` にUI StateごとのPreview（例: データ読み込み中、正常表示、エラー表示）を実装する

### 5. 動作確認と調整
- [ ] ユーザーがSwiftUIをPreviewしエラーやAndroidのレイアウトとの差異を指摘。ユーザーの指摘が全て完了したらタスクを完了とする
- [ ] ユーザーがエミュレータで動作確認し、エラーやおかしな挙動を指摘。動作確認が完了したらタスクを完了とする