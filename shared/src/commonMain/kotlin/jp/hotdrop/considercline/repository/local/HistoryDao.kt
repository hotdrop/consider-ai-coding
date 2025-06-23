package jp.hotdrop.considercline.repository.local

import jp.hotdrop.considercline.db.ConsiderClineDatabase
import jp.hotdrop.considercline.model.PointHistory
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * ポイント履歴のデータアクセスクラス
 */
class HistoryDao(
    private val database: ConsiderClineDatabase
) {

    /**
     * 全ての履歴を取得する
     * @return 履歴のリスト
     */
    suspend fun findAll(): List<PointHistory> {
        return database.historyQueries.selectAll().executeAsList().map { entity ->
            PointHistory(
                dateTime = Instant.parse(entity.dateTime).toLocalDateTime(TimeZone.currentSystemDefault()),
                point = entity.point.toInt(),
                detail = entity.detail
            )
        }
    }

    /**
     * 履歴を保存する
     * @param point ポイント
     * @param detail 詳細
     */
    suspend fun save(point: Int, detail: String) {
        database.historyQueries.insert(
            dateTime = Clock.System.now().toString(),
            point = point.toLong(),
            detail = detail
        )
    }
}
