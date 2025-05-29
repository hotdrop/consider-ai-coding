# 概要
このアプリはポイント獲得や利用をするもので、Kotlin MultiPlatform(KMP)を利用しています。KMPを使用し、AndroidとiOSのビジネスロジックやデータレイヤーをどのように共通化できるか検証するためのアプリです。

# 画面フローのスクリーンショット
![01_start](./images/01_起動処理フロー.png)  
![02_business](./images/02_ポイント獲得と利用フロー.png)


# 雑多メモ
ClineやRoo CodeでKMPをどこまで実装できるのか検証します。
MemoryBank機能を使います。最初はデフォルトのままで試しましたが読み込むファイル数が無駄に多く使われないものもあったので極限まで減らし、`Custom Instruction`ではなくルールファイルで指示するようにしました。
できれば業務でそのまま使いたかったのですが、AndroidViewやStoryboardを実装してもらうのはおそらく不可能に近いので宣言的UIを使うことにしました。ただ、AndroidはAndroidViewも併用できるか検証するため両方使っています。
TODO iOSアプリ側