package jp.hotdrop.considercline.model

/**
 * ポイント情報を保持するデータクラス
 * @property balance 現在のポイント残高
 */
data class Point(
    val balance: Int
) {
    /**
     * 利用可能な最大ポイント
     */
    val maxAvailablePoint: Int
        get() = MAX_POINT - balance

    companion object {
        /**
         * ポイントの最大値
         */
        private const val MAX_POINT = 1000
    }
}
