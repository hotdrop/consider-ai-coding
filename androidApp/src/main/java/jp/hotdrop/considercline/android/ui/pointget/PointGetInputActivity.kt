package jp.hotdrop.considercline.android.ui.pointget

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import dagger.hilt.android.AndroidEntryPoint
import jp.hotdrop.considercline.android.ui.theme.ConsiderClineTheme

@AndroidEntryPoint
class PointGetInputActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConsiderClineTheme {
                PointGetInputScreen(
                    onNavigateToConfirm = { point ->
                        // TODO ポイント確認画面へ遷移する
//                        val intent = Intent(this, PointGetConfirmActivity::class.java).apply {
//                            putExtra(PointGetConfirmActivity.EXTRA_POINT_AMOUNT, point)
//                        }
//                        startActivity(intent)
                    },
                    onBack = { finish() }
                )
            }
        }
    }

    companion object {
        fun startForResult(
            activity: Activity,
            launcher: ActivityResultLauncher<Intent>
        ) = launcher.launch(
            Intent(activity, PointGetInputActivity::class.java)
        )
    }
}
