package jp.hotdrop.considercline.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

/**
 * ポイント履歴を保持するデータクラス
 * @property dateTime 日時
 * @property point ポイント
 * @property detail 詳細
 */
data class PointHistory(
    val dateTime: LocalDateTime,
    val point: Int,
    val detail: String
) {
    /**
     * 日時を文字列に変換する
     * @return yyyy/MM/dd HH:mm形式の文字列
     */
    fun toStringDateTime(): String {
        return dateTime.toString().substring(0, 16).replace('T', ' ')
    }
}
