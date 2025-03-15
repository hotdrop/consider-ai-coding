package jp.hotdrop.considercline.model

/**
 * アプリケーション設定を保持するデータクラス
 * @property userId ユーザーID
 * @property nickName ニックネーム
 * @property email メールアドレス
 */
data class AppSetting(
    val userId: String? = null,
    val nickName: String? = null,
    val email: String? = null
) {
    /**
     * 初期化済みかどうかを確認する
     * @return ユーザーIDが設定されている場合はtrue
     */
    fun isInitialized(): Boolean = userId != null
}
