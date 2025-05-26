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
    fun getMaxAvailablePoint(maxPoint: Int): Int {
        if (maxPoint <= balance) {
            return 0
        }
        return maxPoint - balance
    }
}
